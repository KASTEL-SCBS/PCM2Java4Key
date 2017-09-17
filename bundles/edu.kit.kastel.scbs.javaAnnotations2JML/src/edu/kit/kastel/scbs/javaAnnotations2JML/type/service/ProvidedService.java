package edu.kit.kastel.scbs.javaAnnotations2JML.type.service;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.JmlComment;

/**
 * A provided service. Represents a service of an provided service type and therefore has a self
 * reference as role.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class ProvidedService extends RoleService {

    private static final String SELF_REFERENCE = "this";

    /**
     * Creates a new provided service with the self reference as role and the given name and
     * parameter sources.
     * 
     * @param name
     *            The name of the service.
     * @param parameterSources
     *            The parameter sources of the service.
     */
    public ProvidedService(String name, List<ParameterSource> parameterSources) {
        super(SELF_REFERENCE, name, parameterSources);
    }

    @Override
    public void addNonResultLine(JmlComment comment, List<ParameterSource> nonResultParameterSources) {
        if (!nonResultParameterSources.isEmpty()) {
            comment.addLowIn(getName(), ParameterSource.toString(nonResultParameterSources));
        }
    }

    @Override
    public void addResultLine(JmlComment comment, List<ParameterSource> resultParameterSources) {
        if (!resultParameterSources.isEmpty()) {
            comment.addLowOut(getName(), ParameterSource.toString(resultParameterSources));
        }
    }

    @Override
    protected void addVisibleLine(JmlComment comment) {
        comment.addVisible(getName());
    }
}
