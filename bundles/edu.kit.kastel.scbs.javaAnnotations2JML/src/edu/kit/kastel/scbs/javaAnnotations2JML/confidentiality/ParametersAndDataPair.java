package edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.kit.kastel.scbs.javaAnnotations2JML.EnumConstant;

/**
 * Represents a parameters and data pair of the confidentiality specification.
 * 
 * @author Nils Wilka
 * @version 1.0, 06.08.2017
 */
public class ParametersAndDataPair {

    private EnumConstant enumConstant;

    private List<DataSet> dataSets;

    private List<ParameterSource> parameterSources;

    public ParametersAndDataPair(String simpleName, List<ParameterSource> parameterSources, List<DataSet> dataSets) {
        this.enumConstant = new EnumConstant("ParametersAndDataPair", simpleName);
        this.dataSets = dataSets;
        this.parameterSources = parameterSources;
    }

    public ParametersAndDataPair(EnumConstant enumConstant, List<ParameterSource> parameterSources,
            List<DataSet> dataSets) {
        this.enumConstant = enumConstant;
        this.dataSets = dataSets;
        this.parameterSources = parameterSources;
    }

    public EnumConstant getEnumConstant() {
        return enumConstant;
    }

    public String getEnumConstantSimpleName() {
        return enumConstant.getConstantName();
    }

    public String getEnumConstantFullName() {
        return enumConstant.getFullName();
    }

    public List<DataSet> getDataSets() {
        return dataSets;
    }

    public List<ParameterSource> getParameterSources() {
        return parameterSources;
    }

    public List<ParameterSource> getResultParameterSources() {
        return parameterSources.stream().filter(e -> e.isResult()).collect(Collectors.toList());
    }

    public List<ParameterSource> getNonResultParameterSources() {
        return parameterSources.stream().filter(e -> !e.isResult()).collect(Collectors.toList());
    }

    public static Set<DataSet> unionOfDataSets(List<ParametersAndDataPair> pairs) {
        Set<DataSet> dataSets = new HashSet<>();
        pairs.forEach(e -> dataSets.addAll(e.getDataSets()));
        return dataSets;
    }

    @Override
    public String toString() {
        return "ParametersAndDataPair(dataSets = '" + DataSet.toString(dataSets) + "', parameterSources = '"
                + ParameterSource.toString(parameterSources) + "')";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ParametersAndDataPair) {
            ParametersAndDataPair other = (ParametersAndDataPair) obj;
            return this.enumConstant.equals(other.enumConstant);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
