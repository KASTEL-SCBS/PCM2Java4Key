package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ServiceTypeParser;

/**
 * Command for creating the service types for top level types and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class SetServiceTypes implements Command {

    private TopLevelTypeProvider provider;

    private ServiceTypeAcceptor acceptor;

    /**
     * Creates a new service type setter command with the given top level type provider and service
     * type acceptor to set the new service types.
     * 
     * @param provider
     *            The provider of top level types.
     * 
     * @param acceptor
     *            The acceptor of service types.
     */
    public SetServiceTypes(TopLevelTypeProvider provider, ServiceTypeAcceptor acceptor) {
        this.provider = provider;
        this.acceptor = acceptor;
    }

    @Override
    public void execute() {
        ServiceTypeParser serviceTypeParser = new ServiceTypeParser(provider.getTopLevelTypes());
        List<AbstractServiceType> serviceTypes = null; // TODO
        try {
            serviceTypes = serviceTypeParser.parse();
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        acceptor.setServiceTypes(serviceTypes);
    }
}
