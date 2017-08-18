package edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.AbstractService;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.RequiredService;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.Service;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Concrete class for "service types", in this case a required type. They represent a field of its
 * parent and have the field name as role.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class RequiredServiceType extends AbstractServiceType {

    /**
     * Creates a new required service type with the given role,type and parent.
     * 
     * @param role
     *            The role of this service type, i.e. the field name this type represents.
     * @param type
     *            The type of this service type.
     * @param parent
     *            The parent it was created from/for.
     */
    public RequiredServiceType(String role, TopLevelType type, TopLevelType parent) {
        super(role, type, parent);
    }

    @Override
    protected AbstractService createService(Service service) {
        return new RequiredService(getRole(), service.getName(), service.getParameterSources());
    }
}
