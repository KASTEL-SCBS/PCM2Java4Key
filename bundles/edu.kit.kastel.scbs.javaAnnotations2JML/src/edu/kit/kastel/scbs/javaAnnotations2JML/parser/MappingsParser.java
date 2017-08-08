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
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelTypeMappings;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParametersAndDataPair;

public class MappingsParser extends
        JavaAnnotations2JMLParser<Pair<ConfidentialitySpecification, TopLevelTypeMappings>, TopLevelTypeMappings> {

    public MappingsParser(Pair<ConfidentialitySpecification, TopLevelTypeMappings> source) {
        super(source);
    }

    @Override
    public TopLevelTypeMappings parse() throws ParseException {
        TopLevelTypeMappings tltMappings = getSource().getSecond();
        try {
            setMappings(tltMappings);
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
        setResult(tltMappings);
        return getResult();
    }

    private void setMappings(TopLevelTypeMappings tltMappings) throws JavaModelException, ParseException {
        for (TopLevelType type : tltMappings.unionOfProvidedAndRequiredTypes()) {
            IMethod[] methods = type.getIType().getMethods();

            for (IMethod method : methods) {
                if (Anno2JmlUtil.hasInformationFlowAnnotation(method)) {
                    InformationFlowAnnotation annotation = parseInformationFlowAnnotation(getSource().getFirst(),
                            method);
                    // add mapping type -> (method, annotation)
                    tltMappings.addMethodWithIFToTopLevelType(type, method, annotation);
                    Set<DataSet> dataSets = ParametersAndDataPair
                            .unionOfDataSets(annotation.getParameterAndDataPairs());
                    // for each dataSet: add mapping dataSet -> type
                    dataSets.forEach(e -> tltMappings.addTopLevelTypeAsSpecificationForDataSet(e, type));
                }
            }
        }
    }

    public InformationFlowAnnotation parseInformationFlowAnnotation(ConfidentialitySpecification specification,
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
            ParametersAndDataPair pair = optional.orElseThrow(() -> new ParseException(
                    "Unexpected parameters and data pair in the annotation of the method " + method.getElementName()));
            parametersAndDataPairs.add(pair);
        }
        return parametersAndDataPairs;
    }

    private List<Optional<ParametersAndDataPair>> parseAnnotationAndGetCorrespondingPairs(
            ConfidentialitySpecification specification, IMethod method) throws ParseException {
        InformationFlowAnnotationArguments arguments = new InformationFlowAnnotationParser(method).parse();
        List<Optional<ParametersAndDataPair>> pairCorrespondences = arguments.getParametersAndDataPairEnumConstants()
                .stream().map(e -> specification.getParametersAndDataPairFromEnumConstantName(e.getFullName()))
                .collect(Collectors.toList());
        return pairCorrespondences;
    }
}
