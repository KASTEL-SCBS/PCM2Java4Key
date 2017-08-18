package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.ServiceCreator;

/**
 * Command for creating the services for service types and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class CreateServices implements Command {

    private ServiceTypeProvider provider;

    /**
     * Creates a new service creation command with the given service type provider.
     * 
     * @param provider
     *            The provider of service types.
     */
    public CreateServices(ServiceTypeProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute() {
        new ServiceCreator(provider.getServiceTypes()).parse();
    }
}
