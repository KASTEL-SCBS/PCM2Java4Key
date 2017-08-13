package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;

public class ConcreteServiceProvider implements ServiceProvider {

    private Map<DataSet, List<Service>> services;

    public ConcreteServiceProvider(Map<DataSet, List<Service>> services) {
        this.services = services;
    }

    @Override
    public Set<DataSet> getDataSets() {
        Optional<Set<DataSet>> optional = Optional.ofNullable(services.keySet());
        return optional.orElse(new HashSet<>());
    }

    @Override
    public List<Service> getServices() {
        List<Service> list = new LinkedList<>();
        services.values().forEach(list::addAll);
        return list;
    }

    @Override
    public List<Service> getServices(DataSet dataSet) {
        Optional<List<Service>> optional = Optional.ofNullable(services.get(dataSet));
        return optional.orElse(new LinkedList<Service>());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ConcreteServiceProvider) {
            ConcreteServiceProvider other = (ConcreteServiceProvider) obj;
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
