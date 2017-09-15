package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.MethodProvider;

/**
 * Class for creating services for each given service type. As several different service types may
 * reference the same parent and method provider, this class ensures they all reference the same
 * services via a unique service provider.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class ServicesGenerator {

    private List<AbstractServiceType> serviceTypes;

    private Map<MethodProvider, ServiceProvider> serviceProviderSet;

    /**
     * Creates a new service creator for the given service types.
     * 
     * @param serviceTypes
     *            The service types to add services to.
     */
    public ServicesGenerator(List<AbstractServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
        this.serviceProviderSet = new HashMap<>();
    }

    /**
     * Creates the services for every service type. If there already is a service provider for the
     * current method provider, it will be reused. Else new services are created for the method
     * provider of the service type.
     */
    public void parse() {
        for (AbstractServiceType serviceType : serviceTypes) {
            // look at reference of original unique method provider
            MethodProvider methodProvider = serviceType.getMethodProvider();
            // resolve conflicting service providers:
            // map each service type to a unique service provider
            if (serviceProviderSet.containsKey(methodProvider)) {
                // services have already been created
                serviceType.setServiceProvider(serviceProviderSet.get(methodProvider));
            } else {
                // services have not been created, do that now
                ServiceAcceptor serviceAcceptor = createServices(methodProvider);
                ServiceProvider serviceProvider = serviceAcceptor.getServiceProvider();
                serviceProviderSet.put(methodProvider, serviceProvider);
                serviceType.setServiceProvider(serviceProvider);
            }
        }
    }

    /**
     * Creates services for an service acceptor and gets the service provider at the end.
     * 
     * @param methodProvider
     *            The provider of methods to create the services from.
     * @return An method provider with the services corresponding to the methods.
     */
    private ServiceAcceptor createServices(MethodProvider methodProvider) {
        ServiceAcceptor serviceAcceptor = new ServiceAcceptor();
        Map<IMethod, InformationFlowAnnotation> methods = methodProvider.getMethods();
        methods.keySet().forEach(e -> serviceAcceptor.addService(e, methods.get(e)));
        return serviceAcceptor;
    }
}
