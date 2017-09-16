package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import edu.kit.kastel.scbs.javaAnnotations2JML.transformer.MethodAndAnnotationPairsToServicesTransformer;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.MethodAndServiceContainer;

/**
 * Command for creating the services from the method and information flow annotation pairs for the
 * service types and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 2.0, 16.09.2017
 */
public class GenerateServicesCommand extends Command {

    private final Iterable<MethodAndServiceContainer> providers;

    /**
     * Creates a method to services transformer command with the given method supplier and consumer
     * of services.
     * 
     * @param providers
     *            The supplier of methods and consumer of services.
     */
    public GenerateServicesCommand(final Iterable<MethodAndServiceContainer> providers) {
        this.providers = providers;
    }

    @Override
    public void execute() {
        for (MethodAndServiceContainer provider : providers) {
            final MethodAndAnnotationPairsToServicesTransformer transformer;
            transformer = new MethodAndAnnotationPairsToServicesTransformer(provider);
            provider.addService(transformer.transform());
        }
    }
}
