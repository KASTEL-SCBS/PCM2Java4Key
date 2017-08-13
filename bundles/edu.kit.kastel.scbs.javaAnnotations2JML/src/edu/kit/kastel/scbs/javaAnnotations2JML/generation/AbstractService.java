package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.List;
import java.util.stream.Collectors;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;

public abstract class AbstractService extends Service {

    private String role;

    public AbstractService(String role, String name, List<ParameterSource> parameterSources) {
        super(name, parameterSources);
        this.role = role;
    }

    public String getRole() {
        return role;
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
        return role + "." + super.toString();
    }
}
