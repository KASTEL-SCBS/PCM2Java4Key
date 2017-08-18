package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ConfidentialityRepositoryParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.JavaProjectParser;

/**
 * Command for parsing a java project, i.e. setting the confidentiality package and java types and
 * reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class ParseJavaProject implements Command {

    private IJavaProjectProvider provider;

    /**
     * Creates a new java project parsing command with the given java project provider.
     * 
     * @param provider
     *            The provider of a java project and to set the confidentiality package and java
     *            types.
     */
    public ParseJavaProject(IJavaProjectProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute() {
        IJavaProject javaProject = provider.getIJavaProject();
        JavaProjectParser sourceRepoParser = new JavaProjectParser(javaProject);
        ConfidentialityRepositoryParser confRepoParser = new ConfidentialityRepositoryParser(javaProject);
        try {
            provider.setConfidentialitySpecification(confRepoParser.parse());
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            provider.setTopLevelTypes(sourceRepoParser.parse());
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
