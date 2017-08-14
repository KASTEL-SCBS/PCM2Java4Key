package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;

import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

public interface IJavaProjectProvider {

    public IJavaProject getIJavaProject();

    public void setConfidentialitySpecification(ConfidentialitySpecification specification);

    public void setTopLevelTypes(List<TopLevelType> topLevelTypes);
}
