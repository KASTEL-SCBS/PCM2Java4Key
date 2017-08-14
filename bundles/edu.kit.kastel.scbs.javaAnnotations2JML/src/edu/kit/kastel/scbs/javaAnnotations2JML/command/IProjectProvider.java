package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

public interface IProjectProvider {

    public IProject getIProject();

    public void setIJavaProject(IJavaProject javaProject);
}
