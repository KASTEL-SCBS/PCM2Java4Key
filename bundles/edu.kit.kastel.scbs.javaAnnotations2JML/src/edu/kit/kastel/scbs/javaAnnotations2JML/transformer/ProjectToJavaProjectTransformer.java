package edu.kit.kastel.scbs.javaAnnotations2JML.transformer;

import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;

/**
 * Copies the as source given {@code IProject} and converts it to a {@code IJavaProject}. Therefore
 * the {@code JavaCore.NATURE_ID} must be set.
 * 
 * If the target project already exists it will either be overwritten or a {@code ParseException}
 * will be thrown if it is not part of the workspace.
 * 
 * @author Nils Wilka
 * @version 1.1, 15.09.2017
 */
public class ProjectToJavaProjectTransformer {

    private static final String PROJECT_COPY_POSTFIX = "JML";

    private final IProject project;

    /**
     * Clone of the given source project.
     * 
     * This project gets changed.
     */
    private IProject clone;

    /**
     * Constructs a new transformer with the given project as source.
     * 
     * @param source
     *            The {@code IProject} to scan.
     */
    public ProjectToJavaProjectTransformer(final IProject source) {
        this.project = source;
    }

    /**
     * Copies the as source given {@code IProject} and converts it to a {@code IJavaProject}. If the
     * target project already exists it will either be overwritten or a {@code ParseException} will
     * be thrown if it is not part of the workspace.
     * 
     * @return A copy of the given project as a java project.
     * @throws ParseException
     *             if a {@code CoreException} occurs while deleting the old project or copying.
     */
    public IJavaProject transformProject() throws ParseException {
        copyProjectOverwrite();
        assert clone.exists() : "Project does not exist.";
        return convertToJavaProject();
    }

    /**
     * Copies the source project
     * 
     * @throws ParseException
     *             if a {@code CoreException} occurs while deleting the old project or copying.
     */
    private void copyProjectOverwrite() throws ParseException {
        setProject();
        deleteProjectIfPresent();
        copySourceProject();
    }

    /**
     * Creates the path of the JML project and sets the project.
     */
    private void setProject() {
        final IPath newPath = new Path(project.getName() + PROJECT_COPY_POSTFIX);
        final IWorkspaceRoot workspaceRoot = project.getWorkspace().getRoot();
        clone = workspaceRoot.getProject(newPath.segment(0));
    }

    /**
     * Checks if the target project already exists in the workspace and deletes it.
     * 
     * @throws ParseException
     *             if a {@code CoreException} occurs while deleting.
     */
    private void deleteProjectIfPresent() throws ParseException {
        if (clone.exists()) {
            try {
                clone.delete(true, new NullProgressMonitor());
            } catch (CoreException ce) {
                coreExceptionOccurred(ce);
            }
        }
    }

    /**
     * Copies the source project from the workspace.
     * 
     * @throws ParseException
     *             if a {@code CoreException} occurs while copying.
     */
    private void copySourceProject() throws ParseException {
        try {
            project.copy(new Path(clone.getName()), IResource.DERIVED, new NullProgressMonitor());
        } catch (CoreException ce) {
            coreExceptionOccurred(ce);
        }
    }

    /**
     * Tries to create a java project from the copied workspace project.
     * 
     * @return The java project corresponding to the copied project.
     * @throws ParseException
     *             if a {@code CoreException} occurs in the process of creating the java project or
     *             the copied project does not have the necessary java nature.
     */
    private IJavaProject convertToJavaProject() throws ParseException {
        IJavaProject javaProject;
        try {
            javaProject = createJavaProject();
        } catch (ParseException pe) {
            cleanUp();
            throw new ParseException(pe);
        }
        return javaProject;
    }

    /**
     * Removes the copied project.
     */
    private void cleanUp() {
        try {
            clone.delete(true, new NullProgressMonitor());
        } catch (CoreException e) {
            // ignore
        }
    }

    /**
     * Checks if the previously copied project has the {@code JavaCore.NATURE_ID} set. That means it
     * is a java project and can be processed.
     * 
     * @return The {@code IProject} convert to an {@code IJavaProject}.
     * @throws ParseException
     *             if this project is not a java project or a {@code CoreException} occurs while
     *             checking for it.
     */
    private IJavaProject createJavaProject() throws ParseException {
        IJavaProject javaProject = null;
        try {
            if (clone.hasNature(JavaCore.NATURE_ID)) {
                javaProject = JavaCore.create(clone);
                assert javaProject.exists() : "Java project does not exist.";
            } else {
                throw new ParseException("Not a java project.");
            }
        } catch (CoreException ce) {
            coreExceptionOccurred(ce);
        }
        return javaProject;
    }

    /**
     * Called if a {@code CoreException} is thrown for any reason. Just throws a
     * {@code ParseException} instead.
     * 
     * @param ce
     *            The thrown {@code CoreException} to react to.
     * @throws ParseException
     *             every time.
     */
    private void coreExceptionOccurred(CoreException ce) throws ParseException {
        Optional<String> message = Optional.ofNullable(ce.getMessage());
        throw new ParseException("Core Exception occurred: " + message.orElse("(no error message)"), ce);
    }
}
