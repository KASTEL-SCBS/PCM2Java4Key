package edu.kit.kastel.scbs.javaAnnotations2JML.type.serviceType;

import org.eclipse.jdt.core.IType;

import edu.kit.kastel.scbs.javaAnnotations2JML.type.ServiceProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.service.RequiredService;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.service.RoleService;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.service.Service;

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
     * @param serviceProvider
     *            The serviceProvider to get the services from.
     */
    public RequiredServiceType(String role, IType type, TopLevelType parent, ServiceProvider serviceProvider) {
        super(role, type, parent, serviceProvider);
    }

    @Override
    protected RoleService createService(Service service) {
        return new RequiredService(getRole(), service.getName(), service.getParameterSources());
    }
}
