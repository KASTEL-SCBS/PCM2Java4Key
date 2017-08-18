package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Implementing class provides a method to get top level types.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public interface TopLevelTypeProvider {

    /**
     * Gets the top level types.
     * 
     * @return The top level types.
     */
    public List<TopLevelType> getTopLevelTypes();
}
