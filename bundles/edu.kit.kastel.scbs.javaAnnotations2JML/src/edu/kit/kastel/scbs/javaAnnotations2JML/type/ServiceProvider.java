package edu.kit.kastel.scbs.javaAnnotations2JML.type;

import java.util.List;
import java.util.Set;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.service.Service;

/**
 * Implementing classes provide services, data sets (as services are linked to data sets) and
 * services for a given data set.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public interface ServiceProvider {

    /**
     * Gets all data sets of the implementing class.
     * 
     * @return All data sets of the implementing class.
     */
    public Set<DataSet> getDataSets();

    /**
     * Gets all services of the implementing class.
     * 
     * @return All services of the implementing class.
     */
    public List<Service> getServices();

    /**
     * Gets all services of the implementing class linked to the given data set.
     * 
     * @param dataSet
     *            The data set to get the services for.
     * @return The services of the implementing class linked to the given data set.
     */
    public List<Service> getServices(DataSet dataSet);
}
