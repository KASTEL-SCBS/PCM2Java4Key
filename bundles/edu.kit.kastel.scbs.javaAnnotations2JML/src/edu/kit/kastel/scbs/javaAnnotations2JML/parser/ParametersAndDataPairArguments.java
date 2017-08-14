package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;

public class ParametersAndDataPairArguments {

    private EnumConstant enumConstant;

    private List<EnumConstant> dataSetEnumConstants;

    private List<String> parameterSources;

    public ParametersAndDataPairArguments(String simpleName, List<EnumConstant> dataSetEnumConstants,
            List<String> parameterSources) {
        this.enumConstant = new EnumConstant("ParametersAndDataPairs", simpleName);
        this.dataSetEnumConstants = dataSetEnumConstants;
        this.parameterSources = parameterSources;
    }

    public ParametersAndDataPairArguments(EnumConstant enumConstant, List<EnumConstant> dataSetEnumConstants,
            List<String> parameterSources) {
        this.enumConstant = enumConstant;
        this.dataSetEnumConstants = dataSetEnumConstants;
        this.parameterSources = parameterSources;
    }

    public EnumConstant getEnumConstant() {
        return enumConstant;
    }

    public List<EnumConstant> getDataSets() {
        return dataSetEnumConstants;
    }

    public List<String> getParameterSources() {
        return parameterSources;
    }
}
