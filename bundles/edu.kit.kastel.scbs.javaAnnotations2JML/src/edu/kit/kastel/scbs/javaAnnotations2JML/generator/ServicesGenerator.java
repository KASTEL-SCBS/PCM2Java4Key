package edu.kit.kastel.scbs.javaAnnotations2JML.generator;

import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.MethodAndAnnotationPairProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.service.Service;

/**
 * Class for creating services for each given service type. As several different service types may
 * reference the same parent and method provider, this class ensures they all reference the same
 * services via a unique service provider.
 * 
 * @author Nils Wilka
 * @version 2.0, 16.09.2017
 */
public class ServicesGenerator {

    private final MethodAndAnnotationPairProvider methodProvider;

    private final Consumer<Service> consumer;

    /**
     * Creates a new service creator for the given service types.
     * 
     * @param consumer
     *            The consumer to add services to.
     */
    public ServicesGenerator(MethodAndAnnotationPairProvider methodProvider, Consumer<Service> consumer) {
        this.methodProvider = methodProvider;
        this.consumer = consumer;
    }

    /**
     * Creates the services for every service type. If there already is a service provider for the
     * current method provider, it will be reused. Else new services are created for the method
     * provider of the service type.
     */
    public void parse() {
        Map<IMethod, InformationFlowAnnotation> methods = methodProvider.getMethodAndAnnotationsPairs();
        methods.keySet().forEach(e -> consumer.accept(Service.create(e, methods.get(e))));
    }
}
