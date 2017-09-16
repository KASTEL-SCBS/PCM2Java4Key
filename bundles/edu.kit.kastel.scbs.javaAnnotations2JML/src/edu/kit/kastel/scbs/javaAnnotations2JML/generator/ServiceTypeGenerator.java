package edu.kit.kastel.scbs.javaAnnotations2JML.generator;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.ServiceProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.serviceType.ProvidedServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.serviceType.RequiredServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.Anno2JmlUtil;

/**
 * Used for scanning a given iterable of {@code TopLevelType}s and extracting its provided and
 * required types. Each type with at least one information flow annotation is seen as a relevant and
 * in the result. Other types are ignored.
 * 
 * Gets the service provider corresponding to each {@code IType} via the given function.
 * 
 * Provided types are the implemented interfaces and required types are created from fields.
 * 
 * The given iterable of {@code TopLevelType}s should be unique and their types and fields should
 * preserve that condition.
 * 
 * @author Nils Wilka
 * @version 1.1, 16.09.2017
 */
public class ServiceTypeGenerator
        extends JavaAnnotations2JMLGenerator<Iterable<TopLevelType>, Set<AbstractServiceType>> {

    private final Function<IType, ServiceProvider> iType2ServiceProvider;

    /**
     * Constructs a new generator with the given iterable of {@code TopLevelType}s.
     * 
     * @param source
     *            The iterable of {@code TopLevelType}s to scan.
     * @param iType2ServiceProvider
     *            Gets the service provider corresponding to an {@code IType}.
     */
    public ServiceTypeGenerator(final Iterable<TopLevelType> source,
            final Function<IType, ServiceProvider> iType2ServiceProvider) {
        super(source);
        this.iType2ServiceProvider = iType2ServiceProvider;
    }

    @Override
    protected Set<AbstractServiceType> scanSource() throws ParseException {
        final Set<AbstractServiceType> allAbstractServicesTypes = new HashSet<>();
        for (TopLevelType type : getSource()) {
            // get all provided and required (service) types of this top level type
            final Set<AbstractServiceType> serviceTypeList = parseServiceTypes(type);
            allAbstractServicesTypes.addAll(serviceTypeList);
            // map each top level type to its service types
            type.addServiceTypes(serviceTypeList);
        }
        return allAbstractServicesTypes;
    }

    /**
     * Scans all fields and super types of the given {@code TopLevelType}. All types with an
     * information flow annotation are seen as relevant and all remaining types are ignored.
     * 
     * {@code RequiredServiceType}s are created from fields and {@code ProvidedServiceType}s from
     * the super types. Both are part of the resulting set of {@code AbstractServiceType}s.
     * 
     * @param type
     *            The {@code TopLevelType} to get the fields and super types from.
     * @return A set of {@code AbstractServiceType}s created from the given {@code TopLevelType}.
     * @throws ParseException
     *             if the scanning of the {@code IJavaProject} triggered and
     *             {@code JavaModelException}.
     */
    private Set<AbstractServiceType> parseServiceTypes(final TopLevelType type) throws ParseException {
        final Set<AbstractServiceType> serviceTypes = new HashSet<>();
        try {
            serviceTypes.addAll(getRequiredTopLevelTypeFields(type));
            serviceTypes.addAll(getProvidedTopLevelTypes(type));
        } catch (JavaModelException jme) {
            final Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
        return serviceTypes;
    }

    /**
     * Scans all fields and their types. If a type has at least one information flow annotation, it
     * is seen as relevant and part of the result.
     * 
     * @param topLevelType
     *            The {@code TopLevelType} to be looked at.
     * @return A set of fields which represent all relevant required top level types.
     * @throws JavaModelException
     *             if the scanning of the {@code IJavaProject} triggers them.
     */
    private Set<RequiredServiceType> getRequiredTopLevelTypeFields(final TopLevelType topLevelType)
            throws JavaModelException {
        final Set<RequiredServiceType> requiredTypes = new HashSet<>();
        for (TopLevelType.Field field : topLevelType.getFields()) {
            final IType type = field.getType();
            if (Anno2JmlUtil.hasInformationFlowAnnotation(type)) {
                final ServiceProvider provider = iType2ServiceProvider.apply(type);
                requiredTypes.add(new RequiredServiceType(field.getName(), type, topLevelType, provider));
            }
        }
        return requiredTypes;
    }

    /**
     * Gets the super types with an information flow annotation of the given {@code TopLevelType}.
     * 
     * @param topLevelType
     *            The {@code TopLevelType} to be looked at.
     * @return A set of types which represent all relevant provided top level types.
     * @throws JavaModelException
     *             if the scanning of the {@code IJavaProject} triggers them.
     */
    private Set<ProvidedServiceType> getProvidedTopLevelTypes(final TopLevelType topLevelType)
            throws JavaModelException {
        final Set<ProvidedServiceType> providedTypes = new HashSet<>();
        for (TopLevelType.SuperType superType : topLevelType.getSuperTypeInterfaces()) {
            final IType type = superType.getType();
            if (Anno2JmlUtil.hasInformationFlowAnnotation(type)) {
                final ServiceProvider provider = iType2ServiceProvider.apply(type);
                providedTypes.add(new ProvidedServiceType(type, topLevelType, provider));
            }
        }
        return providedTypes;
    }
}
