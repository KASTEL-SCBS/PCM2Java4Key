package edu.kit.kastel.scbs.javaAnnotations2JML.transformer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.MethodAndAnnotationPairProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.service.Service;

/**
 * Scans the given method and information flow annotation pairs and generates services from them.
 * Also saves data sets as services are linked to data sets.
 * 
 * @author Nils Wilka
 * @version 2.0, 16.09.2017
 */
public class MethodAndAnnotationPairsToServicesTransformer {

    private final Map<DataSet, List<Service>> services;

    private final MethodAndAnnotationPairProvider methodAndAnnotationProvider;

    /**
     * Creates a new method and information flow annotation pairs to services transformer.
     * 
     * @param provider
     *            The provider of methods with information flow annotations.
     */
    public MethodAndAnnotationPairsToServicesTransformer(final MethodAndAnnotationPairProvider provider) {
        this.services = new HashMap<>();
        this.methodAndAnnotationProvider = provider;
    }

    /**
     * Creates the services from the method and information flow annotation pairs.
     * 
     * @return The services created from the method and information flow annotation pairs.
     */
    public Map<DataSet, List<Service>> transform() {
        final Map<IMethod, InformationFlowAnnotation> methods;
        methods = methodAndAnnotationProvider.getMethodAndAnnotationsPairs();
        methods.keySet().forEach(e -> addService(e, methods.get(e)));
        return services;
    }

    /**
     * Gets the services.
     * 
     * @return The services.
     */
    public Map<DataSet, List<Service>> getServices() {
        return this.services;
    }

    /**
     * Creates a service with the given method and information flow annotation and adds it to the
     * services map with the data sets from the information flow annotation.
     * 
     * @param method
     *            The {@code IMethod} to create the service from.
     * @param annotation
     *            The {@code InformationFlowAnnotation} to create the service from.
     */
    private void addService(IMethod method, InformationFlowAnnotation annotation) {
        // remove redundant data sets by using a set
        new HashSet<>(annotation.getDataSets()).forEach(e -> addDataSetToServiceMapAndGetImage(e)
                .add(new Service(method.getElementName(), annotation.getParameterSources(e))));
    }

    /**
     * Gets the image of the given data set. Adds the data set to the service map if it wasn't added
     * before.
     * 
     * @param dataSet
     *            The data set to get the services from and to add.
     * @return The services mapped to the given data set.
     */
    private List<Service> addDataSetToServiceMapAndGetImage(DataSet dataSet) {
        if (!services.containsKey(dataSet)) {
            addDataSet(dataSet);
        }
        return services.get(dataSet);
    }

    /**
     * Adds the given data set.
     * 
     * @param dataSet
     *            The data set to add.
     */
    private void addDataSet(DataSet dataSet) {
        services.put(dataSet, new LinkedList<>());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof MethodAndAnnotationPairsToServicesTransformer) {
            final MethodAndAnnotationPairsToServicesTransformer other = (MethodAndAnnotationPairsToServicesTransformer) obj;
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
