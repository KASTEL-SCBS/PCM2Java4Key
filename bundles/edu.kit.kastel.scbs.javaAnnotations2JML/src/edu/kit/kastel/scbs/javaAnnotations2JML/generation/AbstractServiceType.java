package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;

public abstract class AbstractServiceType {

    private String role;

    // service type for this top level type
    private TopLevelType type;

    // reference to service provider
    private ServiceProvider serviceProvider;

    public AbstractServiceType(String role, TopLevelType type) {
        this.role = role;
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public TopLevelType getType() {
        return type;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    private List<AbstractService> getServicesForDataSet(DataSet dataSet) {
        Optional<List<AbstractService>> optional = Optional.ofNullable(getServices(dataSet));
        return optional.orElse(new LinkedList<>());
    }

    public List<AbstractService> getServices() {
        List<Service> services = serviceProvider.getServices();
        return services.stream().map(e -> createService(e)).collect(Collectors.toList());
    }

    public List<AbstractService> getServices(DataSet dataSet) {
        List<Service> services = serviceProvider.getServices(dataSet);
        return services.stream().map(e -> createService(e)).collect(Collectors.toList());
    }

    public void addServicesForDataSetToJmlComment(DataSet dataSet, JmlComment comment) {
        getServicesForDataSet(dataSet).forEach(e -> e.addServiceToJmlComment(comment));
    }

    protected abstract AbstractService createService(Service service);

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
