package edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.JmlComment;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.ServiceProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.AbstractService;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.Service;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.MethodProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Abstract class for "service types", which are either 'Required Types' or 'Provided Types'. Each
 * service type represents a super type or field of a top level type, which has an information flow
 * annotation. It provides (abstract) services with its role. The role is specified by the sub
 * types.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public abstract class AbstractServiceType implements MethodProvider, ServiceProvider {

    private String role;

    /**
     * The type of this service type.
     */
    private TopLevelType type;

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
     */
    public AbstractServiceType(String role, TopLevelType type, TopLevelType parent) {
        this.role = role;
        this.type = type;
        this.parent = parent;
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
    public TopLevelType getType() {
        return type;
    }

    /**
     * Gets the method provider of this service type. The type which holds the methods this type
     * provides the services for.
     * 
     * @return The method provider of this service type.
     */
    public MethodProvider getMethodProvider() {
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
     * Sets the source service provider, this type delegates to.
     * 
     * @param serviceProvider
     *            The service provider this type gets the services from.
     */
    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
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
    private List<AbstractService> getServicesForDataSet(DataSet dataSet) {
        List<AbstractService> services = getServices(dataSet).stream().map(e -> (AbstractService) e)
                .collect(Collectors.toList());
        Optional<List<AbstractService>> optional = Optional.ofNullable(services);
        return optional.orElse(new LinkedList<>());
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
    protected abstract AbstractService createService(Service service);

    @Override
    public Map<IMethod, InformationFlowAnnotation> getMethods() {
        return getMethodProvider().getMethods();
    }

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
