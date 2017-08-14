package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;

public interface ServiceTypeAcceptor {

    public void setServiceTypes(List<AbstractServiceType> serviceTypes);
}
