package edu.kit.kastel.scbs.javaAnnotations2JML.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.Anno2JmlUtil;

/**
 * Used to generate the information flow annotation of a given method. Then returns the
 * {@code InformationFlowAnnotationArguments} to be used in the context of a
 * {@code ConfidentialitySpecification} to create {@code InformationFlowAnnotation}s.
 * 
 * @author Nils Wilka
 * @version 1.0, 08.08.2017
 */
public class InformationFlowAnnotationGenerator
        extends JavaAnnotations2JMLGenerator<IMethod, InformationFlowAnnotationArguments> {

    /**
     * Creates a new generator with the given method as source.
     * 
     * @param source
     *            The {@code IMethod} to scan.
     */
    public InformationFlowAnnotationGenerator(IMethod source) {
        super(source);
    }

    @Override
    protected InformationFlowAnnotationArguments scanSource() throws ParseException {
        List<EnumConstant> pairEnumConstants;
        try {
            pairEnumConstants = getParametersAndDataPairsEnumConstants(getSource());
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
        return new InformationFlowAnnotationArguments(pairEnumConstants);
    }

    /**
     * First gets all values of the member 'parametersAndDataPairs' from the annotation of the given
     * method and then converts them to {@code EnumConstant}s.
     * 
     * @param method
     *            The method with the information flow annotation
     * @return A list of {@code EnumConstant}s representing {@code ParametersAndDataPair}s.
     * @throws JavaModelException
     *             if retrieving the annotation of the given method causes it.
     * @throws ParseException
     *             if the information flow annotation is empty.
     */
    private List<EnumConstant> getParametersAndDataPairsEnumConstants(IMethod method)
            throws JavaModelException, ParseException {
        List<IMemberValuePair> pairs = Anno2JmlUtil.getIFAnnotationArguments(method);
        List<Object> values = getParametersAndDataPairsValues(pairs);
        if (values.isEmpty()) {
            throw new ParseException(
                    "Insufficient amount of arguments in the information flow annotation of the method "
                            + method.toString());
        }
        return values.stream().map(e -> new EnumConstant((String) e)).collect(Collectors.toList());
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
    private List<Object> getParametersAndDataPairsValues(List<IMemberValuePair> iMemberValuePairs) {
        Optional<IMemberValuePair> filtered = iMemberValuePairs.stream()
                .filter(e -> e.getMemberName().equals("parametersAndDataPairs")).findFirst();
        return Arrays.asList((Object[]) filtered.get().getValue());
    }
}
