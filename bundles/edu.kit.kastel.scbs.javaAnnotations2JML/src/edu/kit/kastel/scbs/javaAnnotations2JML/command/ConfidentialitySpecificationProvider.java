package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;

/**
 * Implementing class provides a method to get the confidentiality specification.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public interface ConfidentialitySpecificationProvider {

    /**
     * Gets the confidentiality specification.
     * 
     * @return The confidentiality specification.
     */
    public ConfidentialitySpecification getConfidentialitySpecification();
}
