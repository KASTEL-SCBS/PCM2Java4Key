package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public final class Anno2JmlUtil {

    public static final String INFORMATION_FLOW_PROPERTY = "InformationFlow";

    /**
     * No instantiation for utility classes.
     */
    private Anno2JmlUtil() {
        ////
    }

    public static List<IMethod> getMethods(final ICompilationUnit unit) throws JavaModelException {
        List<IType> types = Arrays.asList(unit.getTypes());
        List<IMethod> methods = new LinkedList<>();
        for (IType type : types) {
            methods.addAll(Arrays.asList(type.getMethods()));
        }
        return methods;
    }

    public static boolean hasInformationFlowAnnotation(final IType type) throws JavaModelException {
        for (IMethod method : type.getMethods()) {
            if (hasInformationFlowAnnotation(method)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasInformationFlowAnnotation(final IMethod method) throws JavaModelException {
        List<IAnnotation> annotations = Arrays.asList(method.getAnnotations());
        Optional<IAnnotation> annotation = annotations.stream()
                .filter(e -> e.getElementName().equals(INFORMATION_FLOW_PROPERTY)).findAny();
        return annotation.isPresent();
    }

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
