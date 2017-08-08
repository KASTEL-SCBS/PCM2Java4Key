package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

public class RequiredTopLevelType extends AbstractTopLevelType {

    public RequiredTopLevelType(String role, TopLevelType type) {
        super(role, type);
    }

    @Override
    protected AbstractService createService(IMethod method, InformationFlowAnnotation annotation) {
        return new RequiredService(getRole(), method.getElementName(), annotation.getParameterSources());
    }
}
