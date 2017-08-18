package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

/**
 * Implementing class provides a method to get an {@code IProject}.
 * 
 * Additionally provides a method to set the {@code IJavaProject}.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public interface IProjectProvider {

    /**
     * Gets the {@code IProject}.
     * 
     * @return The {@code IProject}.
     */
    public IProject getIProject();

    /**
     * Sets the {@code IJavaProject}.
     * 
     * @param javaProject
     *            The {@code IJavaProject}.
     */
    public void setIJavaProject(IJavaProject javaProject);
}
