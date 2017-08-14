package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.io.IOException;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.JMLCommentsGenerator;

/* generate jml comments for each type and each data set */
public class GenerateJmlComments implements Command {

    private TopLevelTypeProvider provider;

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
