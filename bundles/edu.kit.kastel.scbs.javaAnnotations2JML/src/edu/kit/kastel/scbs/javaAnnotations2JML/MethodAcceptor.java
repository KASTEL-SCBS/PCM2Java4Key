package edu.kit.kastel.scbs.javaAnnotations2JML;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

public interface MethodAcceptor {

    public void addMethod(IMethod method, InformationFlowAnnotation annotation);
}
