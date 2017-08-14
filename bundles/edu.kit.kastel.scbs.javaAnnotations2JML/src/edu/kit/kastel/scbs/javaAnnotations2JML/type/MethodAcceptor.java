package edu.kit.kastel.scbs.javaAnnotations2JML.type;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

/**
 * Implementing class has to provide a method to add {@code IMethod}s with its
 * {@code InformationFlowAnnotation}.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public interface MethodAcceptor {

    /**
     * Adds a given {@code IMethod} and its {@code InformationFlowAnnotation} to this class.
     * 
     * @param method
     *            The {@code IMethod} to add.
     * @param annotation
     *            The {@code InformationFlowAnnotation} corresponding to the {@code IMethod}.
     */
    public void addMethod(IMethod method, InformationFlowAnnotation annotation);
}
