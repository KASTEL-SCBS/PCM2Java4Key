package edu.kit.kastel.scbs.javaAnnotations2JML.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Utility helper class for accessing types and similar of the Java model.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public final class Anno2JmlUtil {

    public static final String INFORMATION_FLOW_PROPERTY = "InformationFlow";

    /**
     * No instantiation for utility classes.
     */
    private Anno2JmlUtil() {
        ////
    }

    /**
     * Checks whether the given {@code IType} has any method with an information flow annotation.
     * 
     * @param type
     *            The {@code IType}s methods to check.
     * @return True if any method of the given type has an information flow annotation.
     * @throws JavaModelException
     *             if accessing the methods and annotations throws them.
     */
    public static boolean hasInformationFlowAnnotation(final IType type) throws JavaModelException {
        for (IMethod method : type.getMethods()) {
            if (hasInformationFlowAnnotation(method)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the given {@code IMethod} has an information flow annotation.
     * 
     * @param method
     *            The method to check.
     * @return True if the given method has an information flow annotation.
     * @throws JavaModelException
     *             if accessing the annotations throws them.
     */
    public static boolean hasInformationFlowAnnotation(final IMethod method) throws JavaModelException {
        List<IAnnotation> annotations = Arrays.asList(method.getAnnotations());
        Optional<IAnnotation> annotation = annotations.stream()
                .filter(e -> e.getElementName().equals(INFORMATION_FLOW_PROPERTY)).findAny();
        return annotation.isPresent();
    }

    /**
     * Gets a list of all member value pairs from all information flow annotations of the given
     * method.
     * 
     * @param method
     *            The method to get the annotations and member value pairs from.
     * @return A List of {@code IMemberValuePair}s from all information flow annotations.
     * @throws JavaModelException
     *             if accessing the annotations or its member value pairs throws them.
     */
    public static List<IMemberValuePair> getIFAnnotationArguments(final IMethod method) throws JavaModelException {
        List<IAnnotation> annotations = Arrays.asList(method.getAnnotations());
        // get all information flow annotations
        List<IAnnotation> informationFlowAnnotations = annotations.stream()
                .filter(e -> e.getElementName().equals(INFORMATION_FLOW_PROPERTY)).collect(Collectors.toList());

        // get all member values pairs of all information flow annotations
        List<IMemberValuePair> memberValuePairs = new LinkedList<>();
        for (IAnnotation annotation : informationFlowAnnotations) {
            memberValuePairs.addAll(Arrays.asList(annotation.getMemberValuePairs()));
        }
        return memberValuePairs;
    }
}
