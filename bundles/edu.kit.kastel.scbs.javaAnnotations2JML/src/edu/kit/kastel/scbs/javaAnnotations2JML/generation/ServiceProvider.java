package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.List;
import java.util.Set;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.service.Service;

public interface ServiceProvider {

    public Set<DataSet> getDataSets();

    public List<Service> getServices();

    public List<Service> getServices(DataSet dataSet);
}
