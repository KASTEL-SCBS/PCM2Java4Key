package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.generator.ServiceTypeGenerator;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.MethodAndServiceContainer;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.ServiceProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.serviceType.AbstractServiceType;

/**
 * Command for creating the service types for top level types and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.2, 14.09.2017
 */
public class GenerateServiceTypesCommand extends Command {

    private final Supplier<Iterable<TopLevelType>> supplier;

    private final Consumer<Iterable<AbstractServiceType>> consumer;

    private final Consumer<MethodAndServiceContainer> containerConsumer;

    private final Map<IType, MethodAndServiceContainer> iType2Provider;

    /**
     * Creates a new service type scanner command with the given top level type supplier and service
     * type consumer to set the new service types.
     * 
     * @param supplier
     *            The supplier of top level types.
     * @param consumer
     *            The consumer of service types.
     * @param containerConsumer
     *            The consumer of method and service providers.
     */
    public GenerateServiceTypesCommand(final Supplier<Iterable<TopLevelType>> supplier,
            final Consumer<Iterable<AbstractServiceType>> consumer,
            final Consumer<MethodAndServiceContainer> containerConsumer) {
        this.supplier = supplier;
        this.consumer = consumer;
        this.containerConsumer = containerConsumer;
        this.iType2Provider = new HashMap<>();
    }

    @Override
    public void execute() {
        final Function<IType, ServiceProvider> func = iType -> {
            MethodAndServiceContainer container = getMethodAndServiceContainer(iType);
            containerConsumer.accept(container);
            return container;
        };
        final ServiceTypeGenerator serviceTypeGenerator = new ServiceTypeGenerator(supplier.get(), func);
        final Set<AbstractServiceType> serviceTypes;
        try {
            serviceTypes = serviceTypeGenerator.generate();
            consumer.accept(serviceTypes);
        } catch (ParseException e) {
            e.printStackTrace();
            abort();
        }
    }

    /**
     * The given {@code IType} might already have a corresponding {@code MethodAndServiceContainer}.
     * If this is the case the already existing type is returned, else the new types is added to the
     * global map.
     * 
     * @param type
     *            The type to possibly have already a corresponding
     *            {@code MethodAndServiceContainer}.
     * @return A new {@code MethodAndServiceContainer} or the already existing.
     * @throws JavaModelException
     *             if accessing the methods of the type causes it.
     */
    private MethodAndServiceContainer getMethodAndServiceContainer(final IType type) {
        // service types with the same IType map to the same container
        if (!iType2Provider.containsKey(type)) {
            final MethodAndServiceContainer masProvider = new MethodAndServiceContainer(type);
            // set the methods to avoid JavaModelExceptions later
            try {
                Arrays.asList(type.getMethods()).forEach(e -> masProvider.addMethod(e));
                iType2Provider.put(type, masProvider);
            } catch (JavaModelException e) {
                e.printStackTrace();
                abort();
            }
        }
        return iType2Provider.get(type);
    }
}
