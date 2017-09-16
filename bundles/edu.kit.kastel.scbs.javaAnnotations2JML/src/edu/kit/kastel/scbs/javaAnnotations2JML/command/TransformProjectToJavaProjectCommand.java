package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.transformer.ProjectToJavaProjectTransformer;

/**
 * Command for setting the java project, i.e. deleting old projects, copying the new one and
 * converting it to a java project and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.2, 14.09.2017
 */
public class TransformProjectToJavaProjectCommand extends Command {

    private final Supplier<IProject> supplier;

    private final Consumer<IJavaProject> consumer;

    /**
     * Creates a new java project transformer command with the given project supplier and java
     * project consumer.
     * 
     * @param supplier
     *            The supplier of an {@code IProject}.
     * @param consumer
     *            The consumer of an {@code IJavaProject}.
     */
    public TransformProjectToJavaProjectCommand(final Supplier<IProject> supplier,
            final Consumer<IJavaProject> consumer) {
        this.supplier = supplier;
        this.consumer = consumer;
    }

    @Override
    public void execute() {
        try {
            consumer.accept(new ProjectToJavaProjectTransformer(supplier.get()).transformProject());
        } catch (ParseException e) {
            e.printStackTrace();
            abort();
        }
    }
}
