package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;

public class ProvidedServiceType extends AbstractServiceType {

    private static final String SELF_REFERENCE = "this";

    public ProvidedServiceType(TopLevelType type) {
        super(SELF_REFERENCE, type);
    }

    @Override
    protected AbstractService createService(Service service) {
        return new ProvidedService(service.getName(), service.getParameterSources());
    }
}
