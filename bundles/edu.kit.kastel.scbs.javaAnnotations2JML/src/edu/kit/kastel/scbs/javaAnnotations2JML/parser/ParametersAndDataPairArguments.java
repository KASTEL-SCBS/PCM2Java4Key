package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;

/**
 * Helper class for the parsing of parameters and data pairs. The values are temporary stored in
 * this class, until they are created in the context of a confidentiality specification.
 * 
 * @author Nils Wilka
 * @version 1.0, 16.08.2017
 */
public class ParametersAndDataPairArguments {

    private EnumConstant enumConstant;

    private List<EnumConstant> dataSetEnumConstants;

    private List<String> parameterSources;

    /**
     * Creates a new container for all parameters and data pair values.
     * 
     * @param simpleName
     *            The name for identification.
     * @param dataSetEnumConstants
     *            The enum constant list representing the data sets.
     * @param parameterSources
     *            The string list representing the parameter sources.
     */
    public ParametersAndDataPairArguments(String simpleName, List<EnumConstant> dataSetEnumConstants,
            List<String> parameterSources) {
        this.enumConstant = new EnumConstant("ParametersAndDataPairs", simpleName);
        this.dataSetEnumConstants = dataSetEnumConstants;
        this.parameterSources = parameterSources;
    }

    /**
     * Creates a new container for all parameters and data pair values.
     * 
     * @param enumConstant
     *            The enum constant representing this parameters and data pair.
     * @param dataSetEnumConstants
     *            The enum constant list representing the data sets.
     * @param parameterSources
     *            The string list representing the parameter sources.
     */
    public ParametersAndDataPairArguments(EnumConstant enumConstant, List<EnumConstant> dataSetEnumConstants,
            List<String> parameterSources) {
        this.enumConstant = enumConstant;
        this.dataSetEnumConstants = dataSetEnumConstants;
        this.parameterSources = parameterSources;
    }

    /**
     * Gets the enum constant representing this parameters and data pair.
     * 
     * @return The enum constant representing this parameters and data pair.
     */
    public EnumConstant getEnumConstant() {
        return enumConstant;
    }

    /**
     * Gets the enum constant list representing the data sets.
     * 
     * @return The enum constant list representing the data sets.
     */
    public List<EnumConstant> getDataSetEnumConstants() {
        return dataSetEnumConstants;
    }

    /**
     * Gets the string list representing the parameter sources.
     * 
     * @return The string list representing the parameter sources.
     */
    public List<String> getParameterSourceStrings() {
        return parameterSources;
    }
}
