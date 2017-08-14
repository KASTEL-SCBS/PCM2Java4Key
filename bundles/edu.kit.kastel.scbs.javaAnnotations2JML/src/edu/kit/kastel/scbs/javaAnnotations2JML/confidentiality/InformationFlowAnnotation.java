package edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality;

import java.util.LinkedList;
import java.util.List;

/**
 * Simplified representation of an java annotation with the name 'InformationFlow'. Java annotations
 * contain a list of member and value pairs: 'member = value'. In our case the only member allowed
 * is 'parametersAndDataPairs' with an array of enum constants of the enum
 * {@code ParameterAndDataPair} as value.
 * 
 * This class can easily be transferred to allow more members and values if required.
 * 
 * @author Nils Wilka
 * @version 1.0, 03.08.2017
 */
public class InformationFlowAnnotation {

    private List<ParametersAndDataPair> parametersAndDataPairs;

    /**
     * Creates an 'InformationFlow' annotation with the given {@code ParameterAndDataPair} value of
     * the 'parametersAndDataPair' member.
     * 
     * @param parametersAndDataPair
     *            The value of the 'parametersAndDataPair' member and the first value of the list.
     */
    public InformationFlowAnnotation(ParametersAndDataPair parametersAndDataPair) {
        this.parametersAndDataPairs = new LinkedList<>();
        this.parametersAndDataPairs.add(parametersAndDataPair);
    }

    /**
     * Creates an 'InformationFlow' annotation with the given {@code ParameterAndDataPair} values of
     * the 'parametersAndDataPair' member.
     * 
     * @param parametersAndDataPairs
     *            The list of values of the 'parametersAndDataPair' member.
     */
    public InformationFlowAnnotation(List<ParametersAndDataPair> parametersAndDataPairs) {
        this.parametersAndDataPairs = parametersAndDataPairs;
    }

    /**
     * Gets the list of values of the 'parametersAndDataPair' member of this 'InformationFlow'
     * annotation.
     * 
     * @return The list of values of the 'parametersAndDataPair' member of this 'InformationFlow'
     *         annotation.
     */
    public List<ParametersAndDataPair> getParameterAndDataPairs() {
        return parametersAndDataPairs;
    }

    /**
     * Gets all parameter sources from all parameters and data pairs of this annotation.
     * 
     * @return all parameter sources from all parameters and data pairs of this annotation.
     */
    public List<ParameterSource> getParameterSources() {
        List<ParameterSource> params = new LinkedList<>();
        parametersAndDataPairs.forEach(e -> params.addAll(e.getParameterSources()));
        return params;
    }

    /**
     * Gets all data sets from all parameters and data pairs of this annotation.
     * 
     * Might have the same data set multiple times.
     * 
     * @return all data sets from all parameters and data pairs of this annotation.
     */
    public List<DataSet> getDataSets() {
        List<DataSet> dataSets = new LinkedList<>();
        parametersAndDataPairs.forEach(e -> dataSets.addAll(e.getDataSets()));
        return dataSets;
    }

    @Override
    public String toString() {
        return "InformationFlow(parameterAndDataPairs = " + parametersAndDataPairs.toString() + ")";
    }
}
