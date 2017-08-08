package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

public class ProvidedTopLevelType extends AbstractTopLevelType {

    private static final String SELF_REFERENCE = "this";

    public ProvidedTopLevelType(TopLevelType type) {
        super(SELF_REFERENCE, type);
    }

    @Override
    protected AbstractService createService(IMethod method, InformationFlowAnnotation annotation) {
        return new ProvidedService(method.getElementName(), annotation.getParameterSources());
    }
}
