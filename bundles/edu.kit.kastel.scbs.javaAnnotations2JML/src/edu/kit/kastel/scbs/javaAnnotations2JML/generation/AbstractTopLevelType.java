package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

public abstract class AbstractTopLevelType {

    private String role;

    private TopLevelType type;

    private Map<DataSet, List<AbstractService>> services;

    public AbstractTopLevelType(String role, TopLevelType type) {
        this.role = role;
        this.type = type;
        this.services = new HashMap<>();
    }

    public String getRole() {
        return role;
    }

    public TopLevelType getType() {
        return type;
    }

    public Map<DataSet, List<AbstractService>> getServices() {
        return services;
    }

    public void addServicesForDataSetToJmlComment(DataSet dataSet, JmlComment comment) {
        getServicesForDataSet(dataSet).forEach(e -> e.addServiceToJmlComment(comment));
    }

    private List<AbstractService> getServicesForDataSet(DataSet dataSet) {
        Optional<List<AbstractService>> optional = Optional.ofNullable(services.get(dataSet));
        return optional.orElse(new LinkedList<>());
    }

    public void addService(IMethod method, InformationFlowAnnotation annotation) {
        annotation.getDataSets().forEach(e -> getDataSetServices(e).add(createService(method, annotation)));
    }

    private List<AbstractService> getDataSetServices(DataSet dataSet) {
        if (!services.containsKey(dataSet)) {
            services.put(dataSet, new LinkedList<>());
        }
        return services.get(dataSet);
    }

    protected abstract AbstractService createService(IMethod method, InformationFlowAnnotation annotation);
}
