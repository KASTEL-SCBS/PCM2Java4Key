package edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstantInterface;

/**
 * Represents a parameters and data pair of the confidentiality specification.
 * 
 * @author Nils Wilka
 * @version 1.1, 14.08.2017
 */
public class ParametersAndDataPair implements EnumConstantInterface {

    private EnumConstant enumConstant;

    private List<DataSet> dataSets;

    private List<ParameterSource> parameterSources;

    /**
     * Creates a new parameters and data pairs with the given name, parameter sources and data sets.
     * 
     * @param simpleName
     *            The name for identification.
     * @param parameterSources
     *            The parameter sources that are part of this pair.
     * @param dataSets
     *            The data sets included in this pair.
     */
    public ParametersAndDataPair(String simpleName, List<ParameterSource> parameterSources, List<DataSet> dataSets) {
        this.enumConstant = new EnumConstant("ParametersAndDataPair", simpleName);
        this.dataSets = dataSets;
        this.parameterSources = parameterSources;
    }

    /**
     * Creates a new parameters and data pairs with the given enum constant, parameter sources and
     * data sets.
     * 
     * @param enumConstant
     *            To get the name of this pair.
     * @param parameterSources
     *            The parameter sources that are part of this pair.
     * @param dataSets
     *            The data sets included in this pair.
     */
    public ParametersAndDataPair(EnumConstant enumConstant, List<ParameterSource> parameterSources,
            List<DataSet> dataSets) {
        this.enumConstant = enumConstant;
        this.dataSets = dataSets;
        this.parameterSources = parameterSources;
    }

    /**
     * Gets the {@code DataSet}s.
     * 
     * @return The {@code DataSet}s.
     */
    public List<DataSet> getDataSets() {
        return dataSets;
    }

    /**
     * Gets the {@code ParameterSource}s.
     * 
     * @return The {@code ParameterSource}s.
     */
    public List<ParameterSource> getParameterSources() {
        return parameterSources;
    }

    /**
     * Gets the enum constant representing the data set.
     * 
     * @return The enum constant representing the data set.
     */
    public EnumConstant getEnumConstant() {
        return enumConstant;
    }

    @Override
    public String getEnumConstantType() {
        return enumConstant.getEnumConstantType();
    }

    @Override
    public String getEnumConstantSimpleName() {
        return enumConstant.getEnumConstantSimpleName();
    }

    @Override
    public String getEnumConstantFullName() {
        return enumConstant.getEnumConstantFullName();
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
