package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.transformer.ProjectToJavaProjectTransformer;

/* copy and get java project */

/**
 * Command for setting the java project, i.e. deleting old projects, copying the new one and
 * converting it to a java project and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.2, 14.09.2017
 */
public class TransformeProjectToJavaProjectCommand extends Command {

    private Supplier<IProject> supplier;

    private Consumer<IJavaProject> consumer;

    /**
     * Creates a new java project setter command with the given project supplier and java project
     * consumer.
     * 
     * @param supplier
     *            The supplier of an {@code IProject}.
     * @param consumer
     *            The consumer of an {@code IJavaProject}.
     */
    public TransformeProjectToJavaProjectCommand(Supplier<IProject> supplier, Consumer<IJavaProject> consumer) {
        this.supplier = supplier;
        this.consumer = consumer;
    }

    @Override
    public void execute() {
        try {
            consumer.accept(new ProjectToJavaProjectTransformer(supplier.get()).transformProject());
        } catch (ParseException e2) {
            e2.printStackTrace();
            abort();
        }
    }
}
