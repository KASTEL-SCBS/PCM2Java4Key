package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

public class ServiceProvider {

    private TopLevelType servicesSource;

    private Map<DataSet, List<Service>> services;

    public ServiceProvider(TopLevelType servicesSource) {
        this.servicesSource = servicesSource;
        this.services = new HashMap<>();
    }

    public TopLevelType getTopLevelType() {
        return servicesSource;
    }

    public Set<DataSet> getDataSets() {
        Optional<Set<DataSet>> optional = Optional.ofNullable(services.keySet());
        return optional.orElse(new HashSet<>());
    }

    // Does not change source type
    public void addDataSet(DataSet dataSet) {
        services.put(dataSet, new LinkedList<>());
    }

    public List<Service> getServices() {
        List<Service> list = new LinkedList<>();
        services.values().forEach(list::addAll);
        return list;
    }

    public List<Service> getServices(DataSet dataSet) {
        Optional<List<Service>> optional = Optional.ofNullable(services.get(dataSet));
        return optional.orElse(new LinkedList<Service>());
    }

    public void addService(IMethod method, InformationFlowAnnotation annotation) {
        // remove redundant data sets
        new HashSet<>(annotation.getDataSets())
                .forEach(e -> addDataSetToServiceMapAndGetImage(e).add(Service.create(method, annotation)));
    }

    private List<Service> addDataSetToServiceMapAndGetImage(DataSet dataSet) {
        if (!services.containsKey(dataSet)) {
            // type -> data set
            addDataSet(dataSet);
        }
        return services.get(dataSet);
    }

    public boolean hasInformationFlowAnnotation() {
        return !services.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ServiceProvider) {
            ServiceProvider other = (ServiceProvider) obj;
            // same type ==> same services
            return this.servicesSource.equals(other.servicesSource);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return servicesSource.hashCode();
    }

    @Override
    public String toString() {
        return getClass().toString() + "(" + services.toString() + ")";
    }
}
