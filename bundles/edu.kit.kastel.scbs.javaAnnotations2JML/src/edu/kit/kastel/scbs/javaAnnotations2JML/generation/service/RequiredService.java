package edu.kit.kastel.scbs.javaAnnotations2JML.generation.service;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.JmlComment;

/**
 * A required service. Represents a service of an required service type and therefore has the
 * required service type role as role.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class RequiredService extends AbstractService {

    /**
     * Creates a new required service with the given role, name and parameter sources.
     * 
     * @param role
     *            The role of this service.
     * @param name
     *            The name of the service.
     * @param parameterSources
     *            The parameter sources of the service.
     */
    public RequiredService(String role, String name, List<ParameterSource> parameterSources) {
        super(role, name, parameterSources);
    }

    @Override
    public void addNonResultLine(JmlComment comment, List<ParameterSource> nonResultParameterSources) {
        if (!nonResultParameterSources.isEmpty()) {
            comment.addDeterminesLine(getRole(), getName(), ParameterSource.toString(nonResultParameterSources));
        }
    }

    @Override
    public void addResultLine(JmlComment comment, List<ParameterSource> resultParameterSources) {
        if (!resultParameterSources.isEmpty()) {
            comment.addByLine(getRole(), getName(), ParameterSource.toString(resultParameterSources));
        }
    }
}
