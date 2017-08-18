package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;

/**
 * Implementing class provides a method to get service types.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public interface ServiceTypeProvider {

    /**
     * Gets the service types.
     * 
     * @return The service types.
     */
    public List<AbstractServiceType> getServiceTypes();
}
