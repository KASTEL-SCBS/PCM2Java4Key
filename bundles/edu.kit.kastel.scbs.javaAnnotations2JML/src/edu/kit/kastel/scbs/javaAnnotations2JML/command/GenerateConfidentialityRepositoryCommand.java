package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.generator.ConfidentialityRepositoryGenerator;

/**
 * Command for scanning a java project, setting the confidentiality package and reacting to
 * exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.09.2017
 */
public class GenerateConfidentialityRepositoryCommand extends Command {

    private Supplier<IJavaProject> supplier;

    private Consumer<ConfidentialitySpecification> csConsumer;

    /**
     * Creates a new confidentiality repository command with the given supplier and consumer.
     * 
     * @param supplier
     *            The supplier of a java project.
     * @param csConsumer
     *            To set the confidentiality package.
     */
    public GenerateConfidentialityRepositoryCommand(Supplier<IJavaProject> supplier,
            Consumer<ConfidentialitySpecification> csConsumer) {
        this.supplier = supplier;
        this.csConsumer = csConsumer;
    }

    @Override
    public void execute() {
        ConfidentialityRepositoryGenerator confRepoParser = new ConfidentialityRepositoryGenerator(supplier.get());
        try {
            csConsumer.accept(confRepoParser.parse());
        } catch (ParseException e1) {
            e1.printStackTrace();
            abort();
        }
    }
}
