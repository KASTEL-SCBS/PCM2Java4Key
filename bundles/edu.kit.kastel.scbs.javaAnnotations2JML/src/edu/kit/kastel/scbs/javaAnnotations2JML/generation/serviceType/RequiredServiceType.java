package edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.AbstractService;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.RequiredService;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.Service;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

public class RequiredServiceType extends AbstractServiceType {

    public RequiredServiceType(String role, TopLevelType type, TopLevelType parent) {
        super(role, type, parent);
    }

    @Override
    protected AbstractService createService(Service service) {
        return new RequiredService(getRole(), service.getName(), service.getParameterSources());
    }
}
