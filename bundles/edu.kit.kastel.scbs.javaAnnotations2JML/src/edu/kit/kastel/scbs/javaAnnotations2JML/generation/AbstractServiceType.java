package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.MethodProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

public abstract class AbstractServiceType implements MethodProvider, ServiceProvider {

    private String role;

    // reference to source type
    // the type this service type belongs to (not its corresponding top level type)
    private TopLevelType type;

    // corresponding top level type
    private TopLevelType parent;

    // reference to service provider
    private ServiceProvider serviceProvider;

    public AbstractServiceType(String role, TopLevelType type, TopLevelType parent) {
        this.role = role;
        this.type = type;
        this.parent = parent;
    }

    public String getRole() {
        return role;
    }

    public TopLevelType getType() {
        return type;
    }

    public MethodProvider getMethodProvider() {
        return parent;
    }

    public TopLevelType getParentType() {
        return parent;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public boolean hasServiceProvider() {
        return Optional.ofNullable(serviceProvider).isPresent();
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

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

    public void addServicesForDataSetToJmlComment(DataSet dataSet, JmlComment comment) {
        getServicesForDataSet(dataSet).forEach(e -> e.addServiceToJmlComment(comment));
    }

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
