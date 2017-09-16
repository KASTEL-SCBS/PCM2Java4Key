package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.function.Supplier;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.generator.MethodAndAnnotationPairsGenerator;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.MethodAndServiceContainer;

/**
 * Command for setting the methods and their information flow annotations of service types and
 * reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.2, 14.09.2017
 */
public class GenerateMethodAndInformationFlowPairsCommand extends Command {

    private final Supplier<Iterable<MethodAndServiceContainer>> supplier;

    private final Supplier<ConfidentialitySpecification> specificationSupplier;

    /**
     * Creates a new method and information flow annotation pairs command with the given container
     * supplier and confidentiality specification supplier.
     * 
     * @param supplier
     *            The supplier of method and service containers which they themselves are suppliers
     *            of methods and consumers of method and annotation pairs.
     * @param specificationSupplier
     *            The supplier of the confidentiality specification.
     */
    public GenerateMethodAndInformationFlowPairsCommand(final Supplier<Iterable<MethodAndServiceContainer>> supplier,
            final Supplier<ConfidentialitySpecification> specificationSupplier) {
        this.supplier = supplier;
        this.specificationSupplier = specificationSupplier;
    }

    @Override
    public void execute() {
        for (MethodAndServiceContainer container : supplier.get()) {
            final MethodAndAnnotationPairsGenerator generator;
            generator = new MethodAndAnnotationPairsGenerator(specificationSupplier.get(),
                    container::addMethodAndAnnotationPair, container::getMethods);
            try {
                generator.generate();
            } catch (ParseException e) {
                e.printStackTrace();
                abort();
                break;
            }
        }
    }
}
