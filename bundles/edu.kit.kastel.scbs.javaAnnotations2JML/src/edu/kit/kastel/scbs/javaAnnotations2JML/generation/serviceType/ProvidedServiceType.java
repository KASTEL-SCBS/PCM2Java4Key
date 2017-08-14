package edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.AbstractService;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.ProvidedService;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.Service;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

public class ProvidedServiceType extends AbstractServiceType {

    private static final String SELF_REFERENCE = "this";

    public ProvidedServiceType(TopLevelType type, TopLevelType parent) {
        super(SELF_REFERENCE, type, parent);
    }

    @Override
    protected AbstractService createService(Service service) {
        return new ProvidedService(service.getName(), service.getParameterSources());
    }
}
