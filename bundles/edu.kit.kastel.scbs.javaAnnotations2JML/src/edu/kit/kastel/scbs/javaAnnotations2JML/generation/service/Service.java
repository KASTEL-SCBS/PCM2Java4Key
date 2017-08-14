package edu.kit.kastel.scbs.javaAnnotations2JML.generation.service;

import java.util.List;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;

public class Service {

    private String name;

    private List<ParameterSource> parameterSources;

    public Service(String name, List<ParameterSource> parameterSources) {
        this.name = name;
        this.parameterSources = parameterSources;
    }

    public String getName() {
        return name;
    }

    public List<ParameterSource> getParameterSources() {
        return parameterSources;
    }

    @Override
    public String toString() {
        return name + "(" + ParameterSource.toString(parameterSources) + ")";
    }

    public static Service create(IMethod method, InformationFlowAnnotation annotation) {
        return new Service(method.getElementName(), annotation.getParameterSources());
    }
}
