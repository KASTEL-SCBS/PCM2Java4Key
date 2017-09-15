package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;
import java.util.function.Supplier;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.ServicesGenerator;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;

/**
 * Command for creating the services for service types and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.1, 14.09.2017
 */
public class GenerateServicesCommand extends Command {

    private Supplier<List<AbstractServiceType>> supplier;

    /**
     * Creates a new service creation command with the given service type supplier.
     * 
     * @param supplier
     *            The provider of service types.
     */
    public GenerateServicesCommand(Supplier<List<AbstractServiceType>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void execute() {
        new ServicesGenerator(supplier.get()).parse();
    }
}
