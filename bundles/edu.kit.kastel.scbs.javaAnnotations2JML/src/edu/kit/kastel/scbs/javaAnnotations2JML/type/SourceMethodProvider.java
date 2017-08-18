package edu.kit.kastel.scbs.javaAnnotations2JML.type;

import java.util.List;

import org.eclipse.jdt.core.IMethod;

/**
 * Implementing class has to provide a method to get all source {@code IMethod}s without additional
 * information about them, which belong to an {@code IType}.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public interface SourceMethodProvider {

    /**
     * Gets a list of source {@code IMethod}s without additional information about them.
     * 
     * @return A list of source {@code IMethod}s without additional information about them.
     */
    public List<IMethod> getSourceMethods();
}
