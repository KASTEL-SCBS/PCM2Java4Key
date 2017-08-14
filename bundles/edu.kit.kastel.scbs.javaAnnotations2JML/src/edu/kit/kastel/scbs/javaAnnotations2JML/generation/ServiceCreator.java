package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.MethodProvider;

public class ServiceCreator {

    private List<AbstractServiceType> serviceTypes;

    private Map<MethodProvider, ServiceProvider> serviceProviderSet;

    public ServiceCreator(List<AbstractServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
        this.serviceProviderSet = new HashMap<>();
    }

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

    public ServiceAcceptor createServices(MethodProvider methodProvider) {
        ServiceAcceptor serviceAcceptor = new ServiceAcceptor();
        Map<IMethod, InformationFlowAnnotation> methods = methodProvider.getMethods();
        for (IMethod method : methods.keySet()) {
            serviceAcceptor.addService(method, methods.get(method));
        }
        return serviceAcceptor;
    }
}
