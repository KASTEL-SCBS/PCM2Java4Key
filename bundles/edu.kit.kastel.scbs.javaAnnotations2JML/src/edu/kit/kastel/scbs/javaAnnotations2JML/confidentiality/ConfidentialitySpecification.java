package edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import edu.kit.kastel.scbs.javaAnnotations2JML.EnumConstant;

public class ConfidentialitySpecification {

    private Set<DataSet> dataSets = new HashSet<>();

    private List<ParametersAndDataPair> parametersAndDataPairs = new LinkedList<>();

    public boolean hasDataSet(DataSet dataSet) {
        return dataSets.contains(dataSet);
    }

    public boolean hasDataSetWithEnumConstantName(String dataSetEnumConstantFullName) {
        return getDataSets().stream().anyMatch(e -> e.getEnumConstantFullName().equals(dataSetEnumConstantFullName));
    }

    public boolean hasDataSetWithEnumConstantName(EnumConstant dataSetEnumConstant) {
        return hasDataSetWithEnumConstantName(dataSetEnumConstant.getFullName());
    }

    public void addDataSet(DataSet dataSet) {
        dataSets.add(dataSet);
    }

    public void addDataSet(final String id, final String name, final String enumConstantName) {
        dataSets.add(new DataSet(id, name, enumConstantName));
    }

    public void addAllDataSets(List<DataSet> dataSets) {
        this.dataSets.addAll(dataSets);
    }

    public Set<DataSet> getDataSets() {
        return dataSets;
    }

    public Optional<DataSet> getDataSetFromEnumConstantName(String dataSetEnumConstantFullName) {
        return getDataSets().stream().filter(e -> e.getEnumConstantFullName().equals(dataSetEnumConstantFullName))
                .findAny();
    }

    public Optional<DataSet> getDataSetFromEnumConstantName(EnumConstant dataSetEnumConstant) {
        return getDataSetFromEnumConstantName(dataSetEnumConstant.getFullName());
    }

    public boolean hasParameterAndDataPair(ParametersAndDataPair parametersAndDataPair) {
        return parametersAndDataPairs.contains(parametersAndDataPair);
    }

    public boolean hasParametersAndDataPairWithEnumConstantName(String pairEnumConstantFullName) {
        return getParameterAndDataPairs().stream()
                .anyMatch(e -> e.getEnumConstantFullName().equals(pairEnumConstantFullName));
    }

    public boolean hasParametersAndDataPairWithEnumConstantName(EnumConstant pairEnumConstant) {
        return hasParametersAndDataPairWithEnumConstantName(pairEnumConstant.getFullName());
    }

    public void addParameterAndDataPair(ParametersAndDataPair parametersAndDataPair) {
        this.parametersAndDataPairs.add(parametersAndDataPair);
    }

    public void addAllParameterAndDataPairs(List<ParametersAndDataPair> parametersAndDataPairs) {
        this.parametersAndDataPairs.addAll(parametersAndDataPairs);
    }

    public List<ParametersAndDataPair> getParameterAndDataPairs() {
        return parametersAndDataPairs;
    }

    public Optional<ParametersAndDataPair> getParametersAndDataPairFromEnumConstantName(
            String parametersAndDataPairId) {
        return getParameterAndDataPairs().stream()
                .filter(e -> e.getEnumConstantFullName().equals(parametersAndDataPairId)).findAny();
    }

    public Optional<ParametersAndDataPair> getParametersAndDataPairFromEnumConstantName(EnumConstant pairEnumConstant) {
        return getParametersAndDataPairFromEnumConstantName(pairEnumConstant.getFullName());
    }
}
