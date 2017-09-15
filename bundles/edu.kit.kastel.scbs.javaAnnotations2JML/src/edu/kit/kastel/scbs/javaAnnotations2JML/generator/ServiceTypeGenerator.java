package edu.kit.kastel.scbs.javaAnnotations2JML.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.ProvidedServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.RequiredServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.Anno2JmlUtil;

/**
 * Used for scanning a given list of {@code TopLevelType}s and extracting their provided and
 * required types. Each type with at least one information flow annotation is seen as a relevant and
 * in the result. Other types are ignored.
 * 
 * Provided types are the implemented interfaces and required types are the types of fields.
 * 
 * The given list of {@code TopLevelType}s should be a unique set and their types and fields should
 * preserve that condition.
 * 
 * @author Nils Wilka
 * @version 1.0, 04.08.2017
 */
public class ServiceTypeGenerator extends JavaAnnotations2JMLGenerator<List<TopLevelType>, List<AbstractServiceType>> {

    /**
     * Constructs a new generator with the given list of {@code TopLevelType}s.
     * 
     * @param source
     *            The list of {@code TopLevelType}s to scan.
     */
    public ServiceTypeGenerator(List<TopLevelType> source) {
        super(source);
        assert new HashSet<>(source).size() == source.size() : "Not a unique set of TopLevelTypes";
    }

    @Override
    protected List<AbstractServiceType> parseSource() throws ParseException {
        List<AbstractServiceType> allAbstractServicesTypes = new ArrayList<>();
        for (TopLevelType type : getSource()) {
            // get all provided and required (service) types of this top level type
            List<AbstractServiceType> serviceTypeList = parseServiceTypes(type);
            allAbstractServicesTypes.addAll(serviceTypeList);
            // map each type to its service types
            type.addServiceTypes(serviceTypeList);
        }
        return allAbstractServicesTypes;
    }

    /**
     * Scans all fields and super types of the given {@code TopLevelType}. All types with an
     * information flow annotation are seen as relevant and all remaining types are ignored.
     * 
     * {@code RequiredServiceType}s are created from fields and {@code ProvidedServiceType}s from
     * the super types. Both are part of the resulting list of {@code AbstractServiceType}s.
     * 
     * @param type
     *            The {@code TopLevelType} to get the fields and super types from.
     * @return A list of{@code AbstractServiceType}s created from the given {@code TopLevelType}.
     * @throws ParseException
     *             if the parsing of the {@code IJavaProject} triggered and
     *             {@code JavaModelException}.
     */
    private List<AbstractServiceType> parseServiceTypes(final TopLevelType type) throws ParseException {
        List<AbstractServiceType> serviceTypes = new LinkedList<>();
        try {
            serviceTypes.addAll(getRequiredTopLevelTypeFields(type));
            serviceTypes.addAll(getProvidedTopLevelTypes(type));
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
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
    private List<RequiredServiceType> getRequiredTopLevelTypeFields(final TopLevelType type) throws JavaModelException {
        List<RequiredServiceType> requiredTypes = new LinkedList<>();
        for (TopLevelType.Field field : type.getFields()) {
            if (Anno2JmlUtil.hasInformationFlowAnnotation(field.getTopLevelType().getIType())) {
                requiredTypes.add(new RequiredServiceType(field.getName(), field.getTopLevelType(), type));
            }
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
    private List<ProvidedServiceType> getProvidedTopLevelTypes(final TopLevelType type) throws JavaModelException {
        List<TopLevelType> implementedInterfaces = type.getSuperTypeInterfaces();

        List<ProvidedServiceType> providedTypes = new LinkedList<>();
        for (TopLevelType serviceType : implementedInterfaces) {
            if (Anno2JmlUtil.hasInformationFlowAnnotation(serviceType.getIType())) {
                providedTypes.add(new ProvidedServiceType(serviceType, type));
            }
        }
        return providedTypes;
    }
}
