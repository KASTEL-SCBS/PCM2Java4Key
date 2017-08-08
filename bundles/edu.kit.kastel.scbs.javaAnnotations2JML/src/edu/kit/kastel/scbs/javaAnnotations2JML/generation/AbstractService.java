package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.List;
import java.util.stream.Collectors;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;

public abstract class AbstractService {

    private String role;

    private String name;

    private List<ParameterSource> parameterSources;

    public AbstractService(String role, String name, List<ParameterSource> parameterSources) {
        this.role = role;
        this.name = name;
        this.parameterSources = parameterSources;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public List<ParameterSource> getParameterSources() {
        return parameterSources;
    }

    public void addServiceToJmlComment(JmlComment comment) {
        List<ParameterSource> params = getParameterSources();
        addResultLine(comment, params.stream().filter(e -> e.isResult()).collect(Collectors.toList()));
        addNonResultLine(comment, params.stream().filter(e -> !e.isResult()).collect(Collectors.toList()));
    }

    protected abstract void addNonResultLine(JmlComment comment, List<ParameterSource> nonResultParameterSources);

    protected abstract void addResultLine(JmlComment comment, List<ParameterSource> resultParameterSources);

    @Override
    public String toString() {
        return role + "." + name + "(" + ParameterSource.toString(parameterSources) + ")";
    }
}
