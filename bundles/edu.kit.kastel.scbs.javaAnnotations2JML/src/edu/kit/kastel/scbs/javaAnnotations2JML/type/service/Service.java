package edu.kit.kastel.scbs.javaAnnotations2JML.type.service;

import java.util.List;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;

/**
 * A service has a name and a list of {@code ParameterSource}s. It is created from an
 * {@code IMethod} and an {@code InformationFlowAnnotation}.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class Service {

    private String name;

    private List<ParameterSource> parameterSources;

    /**
     * Creates a new service with the given name and parameter sources.
     * 
     * @param name
     *            The name of the service.
     * @param parameterSources
     *            The parameter sources of this service.
     */
    public Service(String name, List<ParameterSource> parameterSources) {
        this.name = name;
        this.parameterSources = parameterSources;
    }

    /**
     * Gets the name of the service.
     * 
     * @return The name of the service.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the parameter sources of the service.
     * 
     * @return The parameter sources of the service in a list.
     */
    public List<ParameterSource> getParameterSources() {
        return parameterSources;
    }

    @Override
    public String toString() {
        return name + "(" + ParameterSource.toString(parameterSources) + ")";
    }

    /**
     * Creates a service from an {@code IMethod} and an {@code InformationFlowAnnotation}.
     * 
     * @param method
     *            To get the name from.
     * @param annotation
     *            To get the parameter sources from.
     * @return A service with the name from the given method and the parameter sources from the
     *         information flow annotation.
     */
    public static Service create(IMethod method, InformationFlowAnnotation annotation) {
        return new Service(method.getElementName(), annotation.getParameterSources());
    }
}
