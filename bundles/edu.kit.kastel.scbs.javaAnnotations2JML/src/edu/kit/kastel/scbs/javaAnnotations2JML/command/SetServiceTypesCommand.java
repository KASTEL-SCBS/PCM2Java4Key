package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ServiceTypeParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Command for creating the service types for top level types and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.1, 14.09.2017
 */
public class SetServiceTypesCommand implements Command {

    private Supplier<List<TopLevelType>> supplier;

    private Consumer<List<AbstractServiceType>> consumer;

    /**
     * Creates a new service type setter command with the given top level type supplier and service
     * type consumer to set the new service types.
     * 
     * @param supplier
     *            The supplier of top level types.
     * @param consumer
     *            The consumer of service types.
     */
    public SetServiceTypesCommand(Supplier<List<TopLevelType>> supplier, Consumer<List<AbstractServiceType>> consumer) {
        this.supplier = supplier;
        this.consumer = consumer;
    }

    @Override
    public void execute() {
        ServiceTypeParser serviceTypeParser = new ServiceTypeParser(supplier.get());
        List<AbstractServiceType> serviceTypes = null; // TODO
        try {
            serviceTypes = serviceTypeParser.parse();
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        consumer.accept(serviceTypes);
    }
}
