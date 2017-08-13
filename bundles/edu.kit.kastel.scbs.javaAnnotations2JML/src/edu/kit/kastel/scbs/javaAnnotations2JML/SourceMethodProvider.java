package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public interface SourceMethodProvider {

    public List<IMethod> getSourceMethods() throws JavaModelException;
}
