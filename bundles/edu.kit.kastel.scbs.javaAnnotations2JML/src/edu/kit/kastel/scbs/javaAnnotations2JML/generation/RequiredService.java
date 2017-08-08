package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;

public class RequiredService extends AbstractService {

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
