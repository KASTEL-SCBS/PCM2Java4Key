package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.ProvidedServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.RequiredServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.ServiceProvider;

/**
 * Used for scanning a given list of {@code TopLevelType}s and extracting their provided and
 * required types. Each type with at least one information flow annotation is seen as a relevant and
 * in the result. Other types are ignored.
 * 
 * Provided types are the implemented interfaces and required types are the types of fields.
 * 
 * @author Nils Wilka
 * @version 1.0, 04.08.2017
 */
public class ServiceTypeParser
        extends JavaAnnotations2JMLParser<List<TopLevelType>, Map<ServiceProvider, List<AbstractServiceType>>> {

    /**
     * Constructs a new parser with the given ist of {@code TopLevelType}s.
     * 
     * @param source
     *            The list of {@code TopLevelType}s to scan.
     */
    public ServiceTypeParser(List<TopLevelType> source) {
        super(source);
    }

    @Override
    public Map<ServiceProvider, List<AbstractServiceType>> parseSource() throws ParseException {
        // keep track of all service providers
        ServiceTypeParserHelper container = new ServiceTypeParserHelper();
        for (TopLevelType type : getSource()) {
            // get all provided and required (service) types of this top level type
            // this includes its (new) service provider
            List<AbstractServiceType> serviceTypeList = parseServiceTypes(type);
            for (AbstractServiceType abstractServiceType : serviceTypeList) {
                // resolve conflicting service providers:
                // map each service type to a unique service provider
                container.addServiceType(abstractServiceType);
                // map each type to its service types
                type.addServiceType(abstractServiceType);
            }
        }
        return container.getResult();
    }

    private List<AbstractServiceType> parseServiceTypes(final TopLevelType type) throws ParseException {
        List<AbstractServiceType> serviceTypes = new LinkedList<>();
        serviceTypes.addAll(getRequiredTopLevelTypeFields(type));
        serviceTypes.addAll(getProvidedTopLevelTypes(type));
        return serviceTypes;
    }

    /**
     * Scans all fields and their types. If a type has at least one information flow annotation, it
     * is seen as relevant and part of the result.
     * 
     * @param type
     *            The {@code TopLevelType} to be looked at.
     * @return A list of fields which represent all relevant required top level types.
     * @throws JavaModelException
     *             if the parsing of the {@code IJavaProject} triggers them.
     */
    private List<RequiredServiceType> getRequiredTopLevelTypeFields(final TopLevelType type) throws ParseException {
        List<RequiredServiceType> requiredTypes = new LinkedList<>();
        for (TopLevelType.Field field : type.getFields()) {
            requiredTypes.add(new RequiredServiceType(field.getName(), field.getTopLevelType()));
        }
        return requiredTypes;
    }

    /**
     * Gets the super types with an information flow annotation of the given {@code TopLevelType}.
     * 
     * @param type
     *            The {@code TopLevelType} to be looked at.
     * @return A list of types which represent all relevant provided top level types.
     * @throws JavaModelException
     *             if the parsing of the {@code IJavaProject} triggers them.
     */
    private List<ProvidedServiceType> getProvidedTopLevelTypes(final TopLevelType type) throws ParseException {
        List<TopLevelType> implementedInterfaces = type.getSuperTypeInterfaces();

        List<ProvidedServiceType> providedTypes = new LinkedList<>();
        for (TopLevelType serviceType : implementedInterfaces) {
            providedTypes.add(new ProvidedServiceType(serviceType));
        }
        return providedTypes;
    }

    private static class ServiceTypeParserHelper {

        private Map<ServiceProvider, List<AbstractServiceType>> associatedAbstractServiceTypes;

        private Map<ServiceProvider, ServiceProvider> serviceProviderSet;

        public ServiceTypeParserHelper() {
            associatedAbstractServiceTypes = new HashMap<>();
            serviceProviderSet = new HashMap<>();
        }

        private void addServiceType(AbstractServiceType serviceType) {
            ServiceProvider y = serviceType.getServiceProvider();

            if (serviceProviderSet.containsKey(y)) {
                serviceType.setServiceProvider(serviceProviderSet.get(y));
            } else {
                serviceProviderSet.put(y, y);
                associatedAbstractServiceTypes.put(y, new LinkedList<>());
            }
            associatedAbstractServiceTypes.get(y).add(serviceType);
        }

        private Map<ServiceProvider, List<AbstractServiceType>> getResult() {
            return associatedAbstractServiceTypes;
        }
    }
}
