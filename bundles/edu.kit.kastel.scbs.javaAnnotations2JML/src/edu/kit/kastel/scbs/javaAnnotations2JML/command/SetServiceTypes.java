package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ServiceTypeParser;

/* fourth step: for each type get required and provided types */
public class SetServiceTypes implements Command {

    private TopLevelTypeProvider provider;

    private ServiceTypeAcceptor acceptor;

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
