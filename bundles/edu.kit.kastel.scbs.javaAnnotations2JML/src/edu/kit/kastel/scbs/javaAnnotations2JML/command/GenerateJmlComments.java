package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.io.IOException;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.JMLCommentsGenerator;

/**
 * Command for generating the jml comments and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class GenerateJmlComments implements Command {

    private TopLevelTypeProvider provider;

    /**
     * Creates a new jml generation command with the given top level type provider
     * 
     * @param provider
     *            The provider of top level types to generate comments for.
     */
    public GenerateJmlComments(TopLevelTypeProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute() {
        JMLCommentsGenerator generator = new JMLCommentsGenerator(provider.getTopLevelTypes());
        try {
            generator.transformAllAnnotationsToJml();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
