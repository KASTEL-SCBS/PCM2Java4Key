package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ProjectParser;

/* copy and get java project */

/**
 * Command for setting the java project, i.e. deleting old projects, copying the new one and
 * converting it to a java project and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class SetJavaProject implements Command {

    private IProjectProvider provider;

    /**
     * Creates a new java project setter command with the given project provider.
     * 
     * @param provider
     *            The provider of a project and to set the java project.
     */
    public SetJavaProject(IProjectProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute() {
        try {
            provider.setIJavaProject(new ProjectParser(provider.getIProject()).parse());
        } catch (ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
    }

}
