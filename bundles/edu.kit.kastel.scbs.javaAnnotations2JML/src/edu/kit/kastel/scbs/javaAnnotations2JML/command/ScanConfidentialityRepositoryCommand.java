package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.scanner.ConfidentialityRepositoryScanner;

/**
 * Command for scanning a java project, scanning the confidentiality repository package and creating
 * the confidentiality specification and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.09.2017
 */
public class ScanConfidentialityRepositoryCommand extends Command {

    private final Supplier<IJavaProject> supplier;

    private final Consumer<ConfidentialitySpecification> csConsumer;

    /**
     * Creates a new confidentiality repository command with the given supplier and consumer.
     * 
     * @param supplier
     *            The supplier of a java project.
     * @param csConsumer
     *            To set the confidentiality specification.
     */
    public ScanConfidentialityRepositoryCommand(final Supplier<IJavaProject> supplier,
            final Consumer<ConfidentialitySpecification> csConsumer) {
        this.supplier = supplier;
        this.csConsumer = csConsumer;
    }

    @Override
    public void execute() {
        final ConfidentialityRepositoryScanner scanner = new ConfidentialityRepositoryScanner(supplier.get());
        try {
            csConsumer.accept(scanner.scan());
        } catch (ParseException e) {
            e.printStackTrace();
            abort();
        }
    }
}
