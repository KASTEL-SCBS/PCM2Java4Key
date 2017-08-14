package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.Service;

public class ServiceAcceptor {

    private Map<DataSet, List<Service>> services;

    public ServiceAcceptor() {
        this.services = new HashMap<>();
    }

    // result
    public ServiceProvider getServiceProvider() {
        return new ConcreteServiceProvider(services);
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

    // Does not change source type
    private void addDataSet(DataSet dataSet) {
        services.put(dataSet, new LinkedList<>());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ServiceAcceptor) {
            ServiceAcceptor other = (ServiceAcceptor) obj;
            // same type ==> same services
            return this.services.equals(other.services);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return services.hashCode();
    }

    @Override
    public String toString() {
        return getClass().toString() + "(" + services.toString() + ")";
    }

}
