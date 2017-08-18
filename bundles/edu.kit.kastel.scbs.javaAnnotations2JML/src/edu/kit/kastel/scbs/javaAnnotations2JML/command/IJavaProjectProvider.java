package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;

import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Implementing class provides a method to get an {@code IJavaProject}.
 * 
 * Additionally provides a method to set the confidentiality specification and top level types.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public interface IJavaProjectProvider {

    /**
     * Gets the {@code IJavaProject}.
     * 
     * @return The {@code IJavaProject}.
     */
    public IJavaProject getIJavaProject();

    /**
     * Sets the confidentiality specification.
     * 
     * @param specification
     *            The confidentiality specification.
     */
    public void setConfidentialitySpecification(ConfidentialitySpecification specification);

    /**
     * Sets the top level types.
     * 
     * @param topLevelTypes
     *            The top level types.
     */
    public void setTopLevelTypes(List<TopLevelType> topLevelTypes);
}
