package edu.kit.kastel.scbs.javaAnnotations2JML.type.service;

import java.util.List;
import java.util.stream.Collectors;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.JmlComment;

/**
 * An abstract service extends a service with a role, i.e. the context the service is used in.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public abstract class RoleService extends Service {

    private String role;

    /**
     * Creates a new abstract service with the given role, name and parameter sources.
     * 
     * @param role
     *            The role of the service.
     * @param name
     *            The name of the service.
     * @param parameterSources
     *            The parameter sources of the service.
     */
    public RoleService(String role, String name, List<ParameterSource> parameterSources) {
        super(name, parameterSources);
        this.role = role;
    }

    /**
     * Gets the role of this service type.
     * 
     * @return The role of this service type.
     */
    public String getRole() {
        return role;
    }

    /**
     * Add the service to the given jml comment, i.e. the parameter sources. Differentiates between
     * "result" and "non-result" parameter sources. Sub types decide how to react accordingly.
     * 
     * @param comment
     *            The jml comment to add the services to.
     */
    public void addServiceToJmlComment(JmlComment comment) {
        List<ParameterSource> params = getParameterSources();
        addResultLine(comment, params.stream().filter(e -> e.isResult()).collect(Collectors.toList()));
        addNonResultLine(comment, params.stream().filter(e -> !e.isResult()).collect(Collectors.toList()));
    }

    /**
     * Adds "non-result" parameter sources to the given jml comment.
     * 
     * @param comment
     *            The jml comment to add the parameter sources to.
     * @param nonResultParameterSources
     *            The parameter sources to add as "non-result".
     */
    protected abstract void addNonResultLine(JmlComment comment, List<ParameterSource> nonResultParameterSources);

    /**
     * Adds "result" parameter sources to the given jml comment.
     * 
     * @param comment
     *            The jml comment to add the parameter sources to.
     * @param resultParameterSources
     *            The parameter sources to add as "result".
     */
    protected abstract void addResultLine(JmlComment comment, List<ParameterSource> resultParameterSources);

    @Override
    public String toString() {
        return role + "." + super.toString();
    }
}
