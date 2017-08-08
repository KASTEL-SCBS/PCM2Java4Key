package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;

public class ProvidedService extends AbstractService {

    private static final String SELF_REFERENCE = "this";

    public ProvidedService(String name, List<ParameterSource> parameterSources) {
        super(SELF_REFERENCE, name, parameterSources);
    }

    @Override
    public void addNonResultLine(JmlComment comment, List<ParameterSource> nonResultParameterSources) {
        if (!nonResultParameterSources.isEmpty()) {
            comment.addByLine(getName(), ParameterSource.toString(nonResultParameterSources));
        }
    }

    @Override
    public void addResultLine(JmlComment comment, List<ParameterSource> resultParameterSources) {
        if (!resultParameterSources.isEmpty()) {
            comment.addDeterminesLine(getName(), ParameterSource.toString(resultParameterSources));
        }
    }
}
