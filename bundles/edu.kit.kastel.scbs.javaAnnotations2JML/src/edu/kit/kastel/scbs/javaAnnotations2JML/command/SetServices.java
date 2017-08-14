package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import edu.kit.kastel.scbs.javaAnnotations2JML.generation.ServiceCreator;

public class SetServices implements Command {

    private ServiceTypeProvider provider;

    public SetServices(ServiceTypeProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute() {
        new ServiceCreator(provider.getServiceTypes()).parse();
    }
}
