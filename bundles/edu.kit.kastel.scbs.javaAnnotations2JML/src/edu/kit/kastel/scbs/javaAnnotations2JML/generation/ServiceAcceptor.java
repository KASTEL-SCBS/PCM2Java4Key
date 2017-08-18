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

/**
 * Accepts services and data sets (as services are linked to data sets) to be added to it.
 * 
 * Services are available by a @ {@code ServiceProvider} via the {@code getServiceProvider} method.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class ServiceAcceptor {

    private Map<DataSet, List<Service>> services;

    /**
     * Creates a new service acceptor.
     */
    public ServiceAcceptor() {
        this.services = new HashMap<>();
    }

    /**
     * Creates an {@code ServiceProvider} with the services in this class.
     * 
     * @return An {@code ServiceProvider} with the services in this class.
     */
    public ServiceProvider getServiceProvider() {
        return new ConcreteServiceProvider(services);
    }

    /**
     * Creates a service with the given method and annotation and adds it to the service acceptor.
     * 
     * @param method
     *            The {@code IMethod} to create the service from.
     * @param annotation
     *            The {@code InformationFlowAnnotation} to create the service from.
     */
    public void addService(IMethod method, InformationFlowAnnotation annotation) {
        // remove redundant data sets
        new HashSet<>(annotation.getDataSets())
                .forEach(e -> addDataSetToServiceMapAndGetImage(e).add(Service.create(method, annotation)));
    }

    /**
     * Gets the image of the given data set. Adds the data set to the service acceptor if it wasn't
     * added before.
     * 
     * @param dataSet
     *            The data set to get the services from an to add to the service provider.
     * @return The services mapped to the given data set.
     */
    private List<Service> addDataSetToServiceMapAndGetImage(DataSet dataSet) {
        if (!services.containsKey(dataSet)) {
            addDataSet(dataSet);
        }
        return services.get(dataSet);
    }

    /**
     * Adds the given data set to the service acceptor.
     * 
     * @param dataSet
     *            The data set to add.
     */
    private void addDataSet(DataSet dataSet) {
        // Does not change source type
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
