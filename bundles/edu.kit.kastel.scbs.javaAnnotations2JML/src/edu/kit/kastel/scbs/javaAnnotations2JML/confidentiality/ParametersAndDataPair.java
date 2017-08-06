package edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.kit.kastel.scbs.javaAnnotations2JML.EnumConstant;

public class ParametersAndDataPair {

    private EnumConstant enumConstant;

    private List<DataSet> dataSet;

    private List<ParameterSource> parameterSources;

    public ParametersAndDataPair(String simpleName, List<ParameterSource> parameterSources, List<DataSet> dataSet) {
        this.enumConstant = new EnumConstant("ParametersAndDataPair", simpleName);
        this.dataSet = dataSet;
        this.parameterSources = parameterSources;
    }

    public ParametersAndDataPair(EnumConstant enumConstant, List<ParameterSource> parameterSources,
            List<DataSet> dataSet) {
        this.enumConstant = enumConstant;
        this.dataSet = dataSet;
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
        return dataSet;
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

    @Override
    public String toString() {
        // TODO
        return "( dataSets = '" + dataSet + "', parameterSources = '" + ParameterSource.toString(parameterSources)
                + "')";
    }

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

    public static Set<DataSet> unionOfDataSets(List<ParametersAndDataPair> pairs) {
        Set<DataSet> dataSets = new HashSet<>();
        pairs.forEach(e -> dataSets.addAll(e.getDataSets()));
        return dataSets;
    }

    public static List<ParameterSource> getAllNonResultParameterSources(List<ParametersAndDataPair> pairs) {
        List<ParameterSource> nonResultArguments = new LinkedList<>();
        pairs.forEach(e -> nonResultArguments.addAll(e.getNonResultParameterSources()));
        return nonResultArguments;
    }

    public static List<ParameterSource> getAllResultParameterSources(List<ParametersAndDataPair> pairs) {
        List<ParameterSource> resultArguments = new LinkedList<>();
        pairs.forEach(e -> resultArguments.addAll(e.getResultParameterSources()));
        return resultArguments;
    }

    public static class ParameterSource {

        private static final String RESULT_PREFIX = "result";

        private String name;

        public ParameterSource(String name) {
            this.name = name;
        }

        private boolean isResult() {
            return name.startsWith(RESULT_PREFIX);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof ParameterSource) {
                ParameterSource other = (ParameterSource) obj;
                return this.name.equals(other.name);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static String toString(List<ParameterSource> paramterSources) {
            StringBuilder sb = new StringBuilder(paramterSources.get(0).toString());
            for (int i = 1; i < paramterSources.size(); i++) {
                sb.append(", ").append(paramterSources.get(i));
            }
            return sb.toString();
        }
    }
}
