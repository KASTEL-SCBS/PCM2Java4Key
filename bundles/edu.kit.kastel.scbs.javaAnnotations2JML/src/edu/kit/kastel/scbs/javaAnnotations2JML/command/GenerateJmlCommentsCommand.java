package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.JMLCommentsGenerator;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Command for generating the jml comments and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.1, 14.09.2017
 */
public class GenerateJmlCommentsCommand implements Command {

    private Supplier<List<TopLevelType>> supplier;

    /**
     * Creates a new jml generation command with the given top level type supplier.
     * 
     * @param supplier
     *            The supplier of top level types to generate comments for.
     */
    public GenerateJmlCommentsCommand(Supplier<List<TopLevelType>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void execute() {
        JMLCommentsGenerator generator = new JMLCommentsGenerator(supplier.get());
        try {
            generator.transformAllAnnotationsToJml();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
