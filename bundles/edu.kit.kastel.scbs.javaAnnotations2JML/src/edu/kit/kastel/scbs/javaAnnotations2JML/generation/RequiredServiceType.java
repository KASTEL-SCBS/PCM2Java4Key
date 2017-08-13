package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;

public class RequiredServiceType extends AbstractServiceType {

    public RequiredServiceType(String role, TopLevelType type, TopLevelType parent) {
        super(role, type, parent);
    }

    @Override
    protected AbstractService createService(Service service) {
        return new RequiredService(getRole(), service.getName(), service.getParameterSources());
    }
}
