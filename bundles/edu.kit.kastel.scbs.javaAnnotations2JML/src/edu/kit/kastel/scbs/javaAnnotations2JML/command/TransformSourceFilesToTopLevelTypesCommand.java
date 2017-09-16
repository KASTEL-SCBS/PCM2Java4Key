package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.transformer.SourceFilesToTopLevelTypesTransformer;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Command for scanning a java project and creating the top level types from the containted source
 * files and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.09.2017
 */
public class TransformSourceFilesToTopLevelTypesCommand extends Command {

    private Supplier<IJavaProject> supplier;

    private Consumer<Iterable<TopLevelType>> consumer;

    /**
     * Creates a new source file scanner and top level type generator command with the given
     * supplier and consumer.
     * 
     * @param supplier
     *            The supplier of a java project.
     * @param consumer
     *            To set the java top level types.
     */
    public TransformSourceFilesToTopLevelTypesCommand(final Supplier<IJavaProject> supplier,
            final Consumer<Iterable<TopLevelType>> consumer) {
        this.supplier = supplier;
        this.consumer = consumer;
    }

    @Override
    public void execute() {
        final SourceFilesToTopLevelTypesTransformer transformer;
        transformer = new SourceFilesToTopLevelTypesTransformer(supplier.get());
        try {
            consumer.accept(transformer.transform());
        } catch (ParseException e) {
            e.printStackTrace();
            abort();
        }
    }
}
