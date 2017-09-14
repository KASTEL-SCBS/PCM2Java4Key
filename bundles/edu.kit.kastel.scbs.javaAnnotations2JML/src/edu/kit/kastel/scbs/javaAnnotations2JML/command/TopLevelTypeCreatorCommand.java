package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.JavaProjectParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Command for scanning a java project and creating its top level types and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.09.2017
 */
public class TopLevelTypeCreatorCommand extends Command {

    private Supplier<IJavaProject> supplier;

    private Consumer<List<TopLevelType>> tltConsumer;

    /**
     * Creates a new top level type creator command with the given supplier and consumer.
     * 
     * @param supplier
     *            The supplier of a java project.
     * @param tltConsumer
     *            To set the java top level types.
     */
    public TopLevelTypeCreatorCommand(Supplier<IJavaProject> supplier, Consumer<List<TopLevelType>> tltConsumer) {
        this.supplier = supplier;
        this.tltConsumer = tltConsumer;
    }

    @Override
    public void execute() {
        JavaProjectParser sourceRepoParser = new JavaProjectParser(supplier.get());
        try {
            tltConsumer.accept(sourceRepoParser.parse());
        } catch (ParseException e1) {
            e1.printStackTrace();
            abort();
        }
    }
}
