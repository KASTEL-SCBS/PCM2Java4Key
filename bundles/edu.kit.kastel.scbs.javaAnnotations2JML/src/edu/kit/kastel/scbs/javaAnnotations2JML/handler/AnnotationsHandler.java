package edu.kit.kastel.scbs.javaAnnotations2JML.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.kit.kastel.scbs.javaAnnotations2JML.JavaAnnotations2JML;

/**
 * Handler for the 'Create JML-Comments for Confidentiality Verification' (createJML) event.
 * 
 * Extracts the {@code IProject} from the {@code ISelection} and calls
 * {@code JavaAnnotations2JML#execute(IProject))}.
 * 
 * @author Nils Wilka
 * @version 1.0, 28.07.2017
 */
public class AnnotationsHandler extends AbstractHandler implements IHandler {

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        assert !selection.isEmpty() : "Nothing selected.";
        IProject project = null;

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredselection = (IStructuredSelection) selection;
            Object element = structuredselection.getFirstElement();

            if (element instanceof IJavaProject) {
                project = ((IJavaProject) element).getProject();
            } else if (element instanceof IProject) {
                project = (IProject) element;
            }
        }
        assert project.exists() : "Project does not exist.";
        JavaAnnotations2JML.getSingleton().execute(project);
        return null;
    }
}
