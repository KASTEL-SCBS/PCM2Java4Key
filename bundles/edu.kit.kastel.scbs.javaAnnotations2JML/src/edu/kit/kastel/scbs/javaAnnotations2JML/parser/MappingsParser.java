package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.Anno2JmlUtil;
import edu.kit.kastel.scbs.javaAnnotations2JML.EnumConstant;
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
                    InformationFlowAnnotation annotation = parseInformationFlowAnnotation(method);
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

    private InformationFlowAnnotation parseInformationFlowAnnotation(IMethod method)
            throws JavaModelException, ParseException {
        List<EnumConstant> pairEnumConstants = getParametersAndDataPairsEnumConstants(method);
        ConfidentialitySpecification specification = getSource().getFirst();
        List<Optional<ParametersAndDataPair>> pairCorrespondences = pairEnumConstants.stream()
                .map(e -> specification.getParametersAndDataPairFromEnumConstantName(e.getFullName()))
                .collect(Collectors.toList());

        List<ParametersAndDataPair> parametersAndDataPairs = new LinkedList<>();

        for (Optional<ParametersAndDataPair> optional : pairCorrespondences) {
            ParametersAndDataPair pair = optional.orElseThrow(() -> new ParseException(
                    "Unexpected parameters and data pair in the annotation of the method " + method.getElementName()));
            parametersAndDataPairs.add(pair);
        }
        return new InformationFlowAnnotation(parametersAndDataPairs);
    }

    private List<EnumConstant> getParametersAndDataPairsEnumConstants(IMethod method)
            throws JavaModelException, ParseException {
        IMemberValuePair[] pairs = Anno2JmlUtil.getIFAnnotationArguments(method);

        // TODO remove outer if
        if (expectedMemberValuePairs(pairs)) {
            List<Object> values = getParametersAndDataPairsValues(pairs);

            if (values.isEmpty()) {
                // TODO change to ignore this annotation?
                throw new ParseException(
                        "Insufficient amount of arguments in the information flow annotation of the method "
                                + method.toString());
            }
            return values.stream().map(e -> new EnumConstant((String) e)).collect(Collectors.toList());

        } else {
            // TODO change to ignore this annotation?
            throw new ParseException(
                    "Insufficient amount of arguments in the information flow annotation of the method "
                            + method.toString());
        }
    }

    /**
     * Scans an {@code IMemberValuePair} and searches for the member of the name
     * 'parametersAndDataPairs'. Then collects all values via {@code IMemberValuePair#getValue()} in
     * a List of type Object.
     * 
     * @param iMemberValuePairs
     *            The {@code IMemberValuePair} to look at.
     * @return All values from the member 'parametersAndDataPairs' in a list of type Object.
     */
    private List<Object> getParametersAndDataPairsValues(IMemberValuePair[] iMemberValuePairs) {
        return Arrays.asList(iMemberValuePairs).stream().filter(e -> e.getMemberName().equals("parametersAndDataPairs"))
                .map(e -> ((Object[]) e.getValue())[0]).collect(Collectors.toList());
    }

    private boolean expectedMemberValuePairs(IMemberValuePair[] pairs) {
        // more pairs are allowed
        return pairs != null && pairs.length > 0;
    }
}
