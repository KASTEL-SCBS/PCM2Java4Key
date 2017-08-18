package edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.AbstractService;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.ProvidedService;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.Service;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Concrete class for "service types", in this case a provided type. They represent a super type of
 * its parent and have a self reference as role.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class ProvidedServiceType extends AbstractServiceType {

    private static final String SELF_REFERENCE = "this";

    /**
     * Creates a new provided service type with the self reference as role and the given type and
     * parent.
     * 
     * @param type
     *            The type of this service type.
     * @param parent
     *            The parent it was created from/for.
     */
    public ProvidedServiceType(TopLevelType type, TopLevelType parent) {
        super(SELF_REFERENCE, type, parent);
    }

    @Override
    protected AbstractService createService(Service service) {
        return new ProvidedService(service.getName(), service.getParameterSources());
    }
}
