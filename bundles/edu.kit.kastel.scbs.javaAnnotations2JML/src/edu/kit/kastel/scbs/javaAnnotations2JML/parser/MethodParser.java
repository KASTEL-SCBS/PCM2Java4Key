package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParametersAndDataPair;
import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.MethodAcceptor;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.SourceMethodProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.Anno2JmlUtil;

public class MethodParser {

    ConfidentialitySpecification specification;

    MethodAcceptor methodAcceptor;

    SourceMethodProvider sourceProvider;

    public MethodParser(ConfidentialitySpecification specification, MethodAcceptor methodAcceptor,
            SourceMethodProvider sourceMethodProvider) {
        this.specification = specification;
        this.methodAcceptor = methodAcceptor;
        this.sourceProvider = sourceMethodProvider;
    }

    public void parse() throws ParseException {
        try {
            parseMethodsAndAnnotations(sourceProvider, methodAcceptor);
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
    }

    private void parseMethodsAndAnnotations(SourceMethodProvider source, MethodAcceptor acceptor)
            throws JavaModelException, ParseException {
        List<IMethod> methods = source.getSourceMethods();
        for (IMethod method : methods) {
            // only look at methods with information flow annotation
            if (Anno2JmlUtil.hasInformationFlowAnnotation(method)) {
                InformationFlowAnnotation annotation = parseInformationFlowAnnotation(specification, method);
                // service created from method and annotation
                acceptor.addMethod(method, annotation);
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
                .stream().map(e -> specification.getParametersAndDataPairFromEnumConstantName(e.getEnumConstantFullName()))
                .collect(Collectors.toList());
        return pairCorrespondences;
    }
}
