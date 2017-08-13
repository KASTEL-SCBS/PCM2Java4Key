package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.Anno2JmlUtil;
import edu.kit.kastel.scbs.javaAnnotations2JML.Pair;
import edu.kit.kastel.scbs.javaAnnotations2JML.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParametersAndDataPair;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.ServiceProvider;

public class ServicesParser extends
        JavaAnnotations2JMLParser<Pair<ConfidentialitySpecification, Set<ServiceProvider>>, Set<ServiceProvider>> {

    public ServicesParser(Pair<ConfidentialitySpecification, Set<ServiceProvider>> source) {
        super(source);
    }

    @Override
    protected Set<ServiceProvider> parseSource() throws ParseException {
        try {
            for (ServiceProvider serviceProvider : getSource().getSecond()) {
                // each service provider parses all methods of its (source) top level type
                parseMethodsAndAnnotations(serviceProvider);
            }
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
        return getSource().getSecond();
    }

    private void parseMethodsAndAnnotations(ServiceProvider serviceProvider) throws JavaModelException, ParseException {
        IMethod[] methods = serviceProvider.getTopLevelType().getIType().getMethods();
        for (IMethod method : methods) {
            // only look at methods with information flow annotation
            if (Anno2JmlUtil.hasInformationFlowAnnotation(method)) {
                InformationFlowAnnotation annotation = parseInformationFlowAnnotation(getSource().getFirst(), method);
                // service created from method and annotation
                // and available in top level types via service types via service providers
                serviceProvider.addService(method, annotation);
            }
        }
    }

    private InformationFlowAnnotation parseInformationFlowAnnotation(ConfidentialitySpecification specification,
            IMethod method) throws ParseException {
        List<Optional<ParametersAndDataPair>> pairCorrespondences;
        pairCorrespondences = parseAnnotationAndGetCorrespondingPairs(specification, method);
        List<ParametersAndDataPair> parametersAndDataPairs = retrieveCorrespondencesOrThrowException(method,
                pairCorrespondences);
        return new InformationFlowAnnotation(parametersAndDataPairs);
    }

    private List<ParametersAndDataPair> retrieveCorrespondencesOrThrowException(IMethod method,
            List<Optional<ParametersAndDataPair>> pairCorrespondences) throws ParseException {
        List<ParametersAndDataPair> parametersAndDataPairs = new LinkedList<>();
        for (Optional<ParametersAndDataPair> optional : pairCorrespondences) {
            // throw exception if no fitting ParameterAndDataPair was found before
            // (i.e. optional is empty)
            ParametersAndDataPair pair = optional.orElseThrow(() -> new ParseException(
                    "Unexpected parameters and data pair in the annotation of the method " + method.getElementName()));
            parametersAndDataPairs.add(pair);
        }
        return parametersAndDataPairs;
    }

    private List<Optional<ParametersAndDataPair>> parseAnnotationAndGetCorrespondingPairs(
            ConfidentialitySpecification specification, IMethod method) throws ParseException {
        InformationFlowAnnotationArguments arguments = new InformationFlowAnnotationParser(method).parse();
        // Optional's are empty if there was no fitting ParameterAndDataPair
        List<Optional<ParametersAndDataPair>> pairCorrespondences = arguments.getParametersAndDataPairEnumConstants()
                .stream().map(e -> specification.getParametersAndDataPairFromEnumConstantName(e.getFullName()))
                .collect(Collectors.toList());
        return pairCorrespondences;
    }
}
