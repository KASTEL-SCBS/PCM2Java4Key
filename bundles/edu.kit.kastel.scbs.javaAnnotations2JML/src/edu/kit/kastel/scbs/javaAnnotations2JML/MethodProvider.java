package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.Map;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

public interface MethodProvider {

    public Map<IMethod, InformationFlowAnnotation> getMethods();
}
