package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class ProjectParser extends JavaAnnotations2JMLParser<IProject, IJavaProject> {

    /**
     * Clone of the native project.
     * 
     * This project gets changed.
     */
    private IProject project;

    // TODO first create working copy and make only persistent if successful
    public ProjectParser(final IProject project) {
        super(project);
        // TODO conversion of normal projects to java projects
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

    @Override
    public IJavaProject parse() {
        IJavaProject javaProject = parseJavaProject();
        setResult(javaProject);
        return javaProject;
    }

    private IJavaProject parseJavaProject() {
        IJavaProject javaProject = null; // TODO
        try {
            if (project.hasNature(JavaCore.NATURE_ID)) {
                javaProject = JavaCore.create(project);
                assert javaProject.exists() : "IJavaProject does not exist.";
            } else {
                // TODO
                throw new IllegalArgumentException("Not a java project.");
            }
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return javaProject;
    }
}
