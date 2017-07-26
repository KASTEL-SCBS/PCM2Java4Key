package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    public static IMemberValuePair[][] getIMemberValuePairs(final ICompilationUnit unit) throws JavaModelException {
        List<IMethod> methods = getMethods(unit);
        IMemberValuePair[][] mvps = new IMemberValuePair[methods.size()][];

        for (int i = 0; i < methods.size(); i++) {
            mvps[i] = getIMemberValuePairs(methods.get(i));
        }
        return mvps;
    }

    public static IMemberValuePair[] getIMemberValuePairs(final IMethod method) throws JavaModelException {
        IMemberValuePair[] mvp = null;
        // TODO check again?
        if (hasInformationFlowAnnotation(method)) {
            IAnnotation[] annotations = method.getAnnotations();
            if (annotations != null && annotations.length > 0) {
                for (int i = 0; i < annotations.length; i++) {
                    if (annotations[i].getElementName().equals(INFORMATION_FLOW_PROPERTY)) {
                        mvp = annotations[i].getMemberValuePairs();
                        break;
                    }
                }
            }
        }
        return mvp;
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
        IAnnotation[] annotations = method.getAnnotations();

        if (annotations != null) {
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].getElementName().equals(INFORMATION_FLOW_PROPERTY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static IMemberValuePair[] getIFAnnotationArguments(final IMethod method) throws JavaModelException {
        IMemberValuePair[] mvp;
        IAnnotation[] annotations = method.getAnnotations();

        if (annotations != null) {
            for (IAnnotation annotation : annotations) {
                if (annotation.getElementName().equals(INFORMATION_FLOW_PROPERTY)) {
                    mvp = annotation.getMemberValuePairs();
                    if (mvp != null && mvp.length > 0) { // TODO
                        // assert only one IF property
                        return mvp;
                    }
                }
            }
        }
        return new IMemberValuePair[0];
    }
}
