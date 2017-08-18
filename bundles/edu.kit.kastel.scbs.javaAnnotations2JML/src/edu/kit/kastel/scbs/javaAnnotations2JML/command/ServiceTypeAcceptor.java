package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;

/**
 * Implementing class provides a method to set the service types.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public interface ServiceTypeAcceptor {

    /**
     * Sets the service types.
     * 
     * @param serviceTypes
     *            The service types.
     */
    public void setServiceTypes(List<AbstractServiceType> serviceTypes);
}
