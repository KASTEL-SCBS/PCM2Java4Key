package edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the confidentiality specification with the types from the project confidentiality
 * package.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public class ConfidentialitySpecification {

    private Set<DataSet> dataSets = new HashSet<>();

    private List<ParametersAndDataPair> parametersAndDataPairs = new LinkedList<>();

    /**
     * Adds all given data sets to the specification.
     * 
     * @param dataSets
     *            Data sets to be added to the specification.
     */
    public void addAllDataSets(List<DataSet> dataSets) {
        this.dataSets.addAll(dataSets);
    }

    /**
     * Gets the data sets of the specification.
     * 
     * @return The data sets of the specification.
     */
    public Set<DataSet> getDataSets() {
        return dataSets;
    }

    /**
     * Gets the data set corresponding to the given enum constant full name.
     * 
     * Gets the data set with the equal enum constant full name to be more precise.
     * 
     * @param fullName
     *            The enum constant full name to get the corresponding data set for.
     * @return The data set with the equal enum constant full name.
     */
    public Optional<DataSet> getDataSetFromEnumConstantName(String fullName) {
        return getDataSets().stream().filter(e -> e.equalsEnumConstantFullName(fullName)).findAny();
    }

    /**
     * Adds all given parameters and data pairs to the specification.
     * 
     * @param parametersAndDataPairs
     *            Parameters and data pairs to be added to the specification.
     */
    public void addAllParameterAndDataPairs(List<ParametersAndDataPair> parametersAndDataPairs) {
        this.parametersAndDataPairs.addAll(parametersAndDataPairs);
    }

    /**
     * Gets the parameters and data pairs of the specification.
     * 
     * @return The parameters and data pairs of the specification.
     */
    public List<ParametersAndDataPair> getParameterAndDataPairs() {
        return parametersAndDataPairs;
    }

    /**
     * Gets the parameters and data pair corresponding to the given enum constant full name.
     * 
     * Gets the parameters and data pair with the equal enum constant full name to be more precise.
     * 
     * @param fullName
     *            The enum constant full name to get the corresponding parameters and data pair for.
     * @return The parameters and data pair with the equal enum constant full name.
     */
    public Optional<ParametersAndDataPair> getParametersAndDataPairFromEnumConstantName(String fullName) {
        return getParameterAndDataPairs().stream().filter(e -> e.equalsEnumConstantFullName(fullName)).findAny();
    }
}
