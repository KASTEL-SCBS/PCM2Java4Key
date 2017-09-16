package edu.kit.kastel.scbs.javaAnnotations2JML.type.serviceType;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IType;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.JmlComment;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.ServiceProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.service.RoleService;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.service.Service;

/**
 * Abstract class for "service types", which are either 'Required Types' or 'Provided Types'. Each
 * service type represents a super type or field of a top level type, which has an information flow
 * annotation. It provides (abstract) services with its role. The role is specified by the sub
 * types.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public abstract class AbstractServiceType implements ServiceProvider {

    private String role;

    /**
     * The type of this service type.
     */
    private IType type;

    /**
     * The type this service type belongs to, i.e. which it was created for.
     */
    private TopLevelType parent;

    /**
     * Reference to (source) service provider. Multiple service type can map to the same source
     * service provider.
     */
    private ServiceProvider serviceProvider;

    /**
     * Creates a new service type with the given role, type and parent.
     * 
     * @param role
     *            The role of this service type.
     * @param type
     *            The type of this service type.
     * @param parent
     *            The parent it was created from/for.
     * @param serviceProvider
     *            The service provider this class delegates to with respect to services.
     */
    public AbstractServiceType(String role, IType type, TopLevelType parent, ServiceProvider serviceProvider) {
        this.role = role;
        this.type = type;
        this.parent = parent;
        this.serviceProvider = serviceProvider;
    }

    /**
     * Gets the role of this service type.
     * 
     * @return The role of this service type.
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the type of this service type.
     * 
     * @return The type of this service type.
     */
    public IType getType() {
        return type;
    }

    /**
     * Gets the parent of this service type. The type this service type was created from/for.
     * 
     * @return The parent of this service type.
     */
    public TopLevelType getParentType() {
        return parent;
    }

    /**
     * Gets the source service provider. Multiple service type can map to the same source service
     * provider.
     * 
     * @return The source service provider, this type delegates to.
     */
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    /**
     * Checks whether the service provider was already set.
     * 
     * @return True if the service provider was set, else false.
     */
    public boolean hasServiceProvider() {
        return Optional.ofNullable(serviceProvider).isPresent();
    }

    /**
     * Gets all abstract services that are mapped to the given data set.
     * 
     * Returns a new list of there are no services for this data set.
     * 
     * @param dataSet
     *            The data set to get the services for.
     * @return The abstract services that are mapped to the given data set or an empty list if there
     *         are non.
     */
    private List<RoleService> getServicesForDataSet(DataSet dataSet) {
        Optional<List<RoleService>> optional = Optional.ofNullable(getRoleServices(dataSet));
        return optional.orElse(new LinkedList<>());
    }

    /**
     * Gets all role services linked to the given data set.
     * 
     * @param dataSet
     *            The data set to get the role services for.
     * @return The role services linked to the given data set.
     */
    private List<RoleService> getRoleServices(DataSet dataSet) {
        List<Service> services = serviceProvider.getServices();
        return services.stream().map(e -> createService(e)).collect(Collectors.toList());
    }

    @Override
    public Set<DataSet> getDataSets() {
        return serviceProvider.getDataSets();
    }

    @Override
    public List<Service> getServices() {
        List<Service> services = serviceProvider.getServices();
        return services.stream().map(e -> createService(e)).collect(Collectors.toList());
    }

    @Override
    public List<Service> getServices(DataSet dataSet) {
        List<Service> services = serviceProvider.getServices(dataSet);
        return services.stream().map(e -> createService(e)).collect(Collectors.toList());
    }

    /**
     * Adds services this service type provides to the given jml comment. Adds only those services,
     * which hold information about the given data set.
     * 
     * @param dataSet
     *            The data set to get the services for.
     * @param comment
     *            The jml comment to add the services to.
     */
    public void addServicesForDataSetToJmlComment(DataSet dataSet, JmlComment comment) {
        getServicesForDataSet(dataSet).forEach(e -> e.addServiceToJmlComment(comment));
    }

    /**
     * Creates an {@code AbstractService} from the given {@code Service} with the role of this
     * service type.
     * 
     * Sub classes specify what kind of {@code AbstractService} to create.
     * 
     * @param service
     *            The {@code Service} to create this {@code AbstractService} from.
     * @return An {@code AbstractService} from the given service and with the role of this type.
     */
    protected abstract RoleService createService(Service service);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof AbstractServiceType) {
            AbstractServiceType other = (AbstractServiceType) obj;
            return this.role.equals(other.role) && this.type.equals(other.type);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    @Override
    public String toString() {
        return getClass().toString() + "(" + role + "::" + type + ")";
    }
}
