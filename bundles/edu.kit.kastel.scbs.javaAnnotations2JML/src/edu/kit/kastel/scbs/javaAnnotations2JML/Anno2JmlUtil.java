package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
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

    public static String getDataSet(final IMethod method) throws JavaModelException {
        IMemberValuePair[] mvp = getIMemberValuePairs(method);
        for (IMemberValuePair pair : mvp) {
            if (pair.getMemberName().equals("dataSets")) {
                // return ((DataSets) pair.getValue()).get; TODO
                return "deliveryData";
            }
        }
        return null; // TODO
    }

    public static List<ICompilationUnit> getSourceFiles(final IJavaProject javaProject) {
        List<ICompilationUnit> sourceFiles = new LinkedList<>();
        IPackageFragment[] fragments = null;

        try {
            fragments = javaProject.getPackageFragments();
        } catch (JavaModelException e) {
            System.out.println(e.getJavaModelStatus().getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (IPackageFragment fragment : fragments) {
            try {
                sourceFiles.addAll(Arrays.asList(fragment.getCompilationUnits()));
            } catch (JavaModelException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sourceFiles;
    }

    public static List<IMethod> getMethods(final ICompilationUnit unit) throws JavaModelException {
        List<IType> types = Arrays.asList(unit.getTypes());
        List<IMethod> methods = new LinkedList<>();

        for (IType type : types) {
            methods.addAll(Arrays.asList(type.getMethods()));
        }
        return methods;
    }

    @Deprecated
    public static List<ICompilationUnit> getInterfaces(final List<ICompilationUnit> sourceFiles)
            throws JavaModelException {
        List<ICompilationUnit> interfaceFiles = new LinkedList<>();

        for (ICompilationUnit unit : sourceFiles) {
            for (IType type : unit.getTypes()) {
                if (type.isInterface()) {
                    interfaceFiles.add(unit);
                }
            }
        }
        return interfaceFiles;
    }

    @Deprecated
    public static boolean hasInformationFlowAnnotation(final ICompilationUnit unit) throws JavaModelException {
        return hasInformationFlowAnnotation(getMethods(unit));
    }

    @Deprecated
    public static boolean hasInformationFlowAnnotation(final List<IMethod> methods) throws JavaModelException {
        for (IMethod method : methods) {
            if (hasInformationFlowAnnotation(method)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasInformationFlowAnnotation(IMethod method) throws JavaModelException {
        IAnnotation[] annotations = method.getAnnotations();

        if (annotations != null && annotations.length > 0) {
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].getElementName().equals(INFORMATION_FLOW_PROPERTY)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Deprecated
    public static List<IMethod> getInformationFlowAnnotationMethods(final List<IMethod> methods)
            throws JavaModelException {
        List<IMethod> relevantMethods = new LinkedList<>();
        for (IMethod method : methods) {
            if (hasInformationFlowAnnotation(method)) {
                relevantMethods.add(method);
            }
        }
        return relevantMethods;
    }

    @Deprecated
    public static List<IMethod> getInformationFlowAnnotationMethods(final ICompilationUnit unit)
            throws JavaModelException {
        return getInformationFlowAnnotationMethods(getMethods(unit));
    }

    @Deprecated
    public static List<ICompilationUnit> getRelevantInteraces(final List<ICompilationUnit> interfaceFiles)
            throws JavaModelException {
        List<ICompilationUnit> newFiles = new LinkedList<>();
        for (ICompilationUnit unit : interfaceFiles) {
            if (hasInformationFlowAnnotation(unit)) {
                newFiles.add(unit);
            }
        }
        return newFiles;
    }
}
