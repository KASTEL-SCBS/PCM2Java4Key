package edu.kit.kastel.scbs.javaAnnotations2JML.type;

import java.util.Map;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

/**
 * Implementing class has to provide a method to get all {@code IMethod}s with their
 * {@code InformationFlowAnnotation} as a map.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public interface MethodAndAnnotationPairProvider {

    /**
     * Gets a map of {@code IMethod}s and its {@code InformationFlowAnnotation}.
     * 
     * @return Maps an {@code IMethod} to its corresponding {@code InformationFlowAnnotation}.
     */
    public Map<IMethod, InformationFlowAnnotation> getMethodAndAnnotationsPairs();
}
