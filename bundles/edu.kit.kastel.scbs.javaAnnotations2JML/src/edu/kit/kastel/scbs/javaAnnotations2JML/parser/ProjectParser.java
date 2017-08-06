package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import edu.kit.kastel.scbs.javaAnnotations2JML.ParseException;

/**
 * Copies the as source given {@code IProject} and converts it to a {@code IJavaProject}.
 * 
 * Therefore the {@code JavaCore.NATURE_ID} must be set.
 * 
 * @author Nils Wilka
 * @version 1.0, 28.07.2017
 */
public class ProjectParser extends JavaAnnotations2JMLParser<IProject, IJavaProject> {

    /**
     * Clone of the given source project.
     * 
     * This project gets changed.
     */
    private IProject project;

    /**
     * Constructs a new parser with the given project as source.
     * 
     * @param source
     *            The {@code IProject} to scan.
     */
    public ProjectParser(final IProject source) {
        super(source);
    }

    @Override
    public IJavaProject parse() throws ParseException {
        // TODO first create working copy and make only persistent if successful
        // TODO conversion of normal projects to java projects
        this.project = copyProject(getSource());
        IJavaProject javaProject = parseJavaProject();
        setResult(javaProject);
        return javaProject;
    }

    /**
     * Copies a given project.
     * 
     * @param project
     *            The project to be copied
     * @return The copied project.
     * @throws ParseException
     *             if a {@code CoreException} occurs while copying.
     */
    private IProject copyProject(final IProject project) throws ParseException {
        IPath newPath = new Path(project.getName() + "2"); // TODO
        try {
            // copy project
            // TODO: check if new path is unique
            project.copy(newPath, false, new NullProgressMonitor());
        } catch (CoreException ce) {
            Optional<String> message = Optional.ofNullable(ce.getMessage());
            throw new ParseException("Java Core Exception occurred: " + message.orElse("(no error message)"), ce);
        }
        IWorkspaceRoot workspaceRoot = project.getWorkspace().getRoot();
        assert newPath.segmentCount() == 1 : "Cloned project should have exactly one segment.";
        String projectName = newPath.segment(0);
        return workspaceRoot.getProject(projectName);
    }

    /**
     * Checks if the previously copied project has the {@code JavaCore.NATURE_ID} set. That means it
     * is a java project and can be processed.
     * 
     * Then converts it to a java project.
     * 
     * @return The {@code IProject} convert to an {@code IJavaProject}.
     * @throws ParseException
     *             if this project is not a java project or a {@code CoreException} occurs while
     *             checking for it.
     */
    private IJavaProject parseJavaProject() throws ParseException {
        IJavaProject javaProject;
        try {
            if (project.hasNature(JavaCore.NATURE_ID)) {
                javaProject = JavaCore.create(project);
                assert javaProject.exists() : "IJavaProject does not exist.";
            } else {
                throw new ParseException("Not a java project.");
            }
        } catch (CoreException ce) {
            Optional<String> message = Optional.ofNullable(ce.getMessage());
            throw new ParseException("Java Core Exception occurred: " + message.orElse("(no error message)"), ce);
        }
        return javaProject;
    }
}
