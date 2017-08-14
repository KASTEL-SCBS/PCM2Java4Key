package edu.kit.kastel.scbs.javaAnnotations2JML.type;

import org.eclipse.jdt.core.IMethod;

/**
 * Implementing class has to provide a method to add source {@code IMethod}s} without any additional
 * information.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public interface SourceMethodAcceptor {

    /**
     * Adds a given {@code IMethod} without any additional information to this class.
     * 
     * @param method
     *            The source {@code IMethod} to add.
     */
    public void addSourceMethod(IMethod method);
}
