package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class ProjectParser {

    /**
     * The native project.
     * 
     * Just a reference, does not get changed.
     */
    private final IProject nativeProject;

    /**
     * Clone of the native project.
     * 
     * This project gets changed.
     */
    private IProject project;

    /**
     * List of {@code org.eclipse.jdt.core.ICompilationUnit}
     * 
     * Each ICompilationUnit corresponds to a source file in the java project.
     */
    private List<ICompilationUnit> sourceFiles;

    /**
     * i.e. enum, class, interface, ...
     */
    private List<TopLevelType> javaTopLevelTypes;

    /**
     * All interfaces in this project
     */
    private List<TopLevelType> javaInterfaces;

    /**
     * interfaces with information flow properties
     */
    private List<TopLevelType> javaInterfacesWithIFAnnotation;

    /**
     * TODO
     */
    private List<TopLevelType> javaTopLevelTypesWithIFAnnotation;

    // TODO first create working copy and make only persistent if successful
    public ProjectParser(final IProject project) {
        // TODO conversion of normal projects to java projects
        this.nativeProject = project;
        this.project = copyProject(project);
    }

    private IProject copyProject(final IProject project) {
        IPath newPath = new Path(project.getName() + "2"); // TODO
        try {
            // copy project
            // TODO: check if new path is unique
            project.copy(newPath, false, new NullProgressMonitor());

        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        IWorkspaceRoot workspaceRoot = project.getWorkspace().getRoot();
        assert newPath.segmentCount() == 1 : "Cloned project should have exactly one segment.";
        String projectName = newPath.segment(0);
        return workspaceRoot.getProject(projectName);
    }

    public void parse() {
        parseJavaProject();
        try {
            setTopLevelTypes();
            transformAllAnnotationsToJml();
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void parseJavaProject() {
        try {
            if (project.hasNature(JavaCore.NATURE_ID)) {
                final IJavaProject javaProject = JavaCore.create(project);
                assert javaProject.exists() : "IJavaProject does not exist.";
                sourceFiles = Anno2JmlUtil.getSourceFiles(javaProject);
            } else {
                // TODO
                throw new IllegalArgumentException("Not a java project.");
            }
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setTopLevelTypes() throws JavaModelException {
        // TODO JavaModelException
        javaInterfacesWithIFAnnotation = new LinkedList<>();
        javaInterfaces = new LinkedList<>();
        javaTopLevelTypes = new LinkedList<>();
        javaTopLevelTypesWithIFAnnotation = new LinkedList<>();
        for (ICompilationUnit unit : sourceFiles) {
            List<TopLevelType> tltList = TopLevelType.create(unit);
            javaTopLevelTypes.addAll(tltList);
            for (TopLevelType tlt : tltList) {
                if (tlt.hasInformationFlowAnnotation()) {
                    javaTopLevelTypesWithIFAnnotation.add(tlt);
                    if (tlt.getIType().isInterface()) {
                        javaInterfaces.add(tlt);
                        javaInterfacesWithIFAnnotation.add(tlt);
                    }
                } else {
                    if (tlt.getIType().isInterface()) {
                        javaInterfaces.add(tlt);
                    }
                }
            }
        }
    }

    private void transformAllAnnotationsToJml() throws JavaModelException {
        // TODO JavaModelException
        for (TopLevelType topLevelType : javaInterfacesWithIFAnnotation) {
            List<TopLevelType> requiredTopLevelTypes = getRequiredTopLevelTypes(topLevelType);
            List<TopLevelType> providedTopLevelTypes = getProvidedTopLevelTypes(topLevelType);
            topLevelType.transformAnnotationsToJml(requiredTopLevelTypes, providedTopLevelTypes);
        }
    }

    private List<TopLevelType> getRequiredTopLevelTypes(final TopLevelType topLevelType) throws JavaModelException {
        List<TopLevelType> fieldTypes = TopLevelType.create(topLevelType.getAllIFieldITypes());
        List<TopLevelType> fieldTypesWithIFProperty = new LinkedList<>();
        for (TopLevelType fieldTopLevelType : fieldTypes) {
            if (javaTopLevelTypesWithIFAnnotation.contains(fieldTopLevelType)) {
                // if its an top level type and has information flow annotations
                // then it is required.
                fieldTypesWithIFProperty.add(fieldTopLevelType);
            }
        }
        return fieldTypesWithIFProperty;
    }

    private List<TopLevelType> getProvidedTopLevelTypes(final TopLevelType topLevelType) throws JavaModelException {
        List<IType> implementedInterfaces = topLevelType.getSuperTypeInterfacesWithIFProperty();
        return TopLevelType.create(implementedInterfaces);
    }
}