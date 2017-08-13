package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.Anno2JmlUtil;
import edu.kit.kastel.scbs.javaAnnotations2JML.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.ProvidedServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.RequiredServiceType;

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
public class ServiceTypeParser extends JavaAnnotations2JMLParser<List<TopLevelType>, List<AbstractServiceType>> {

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

    private List<AbstractServiceType> parseServiceTypes(final TopLevelType type) throws ParseException {
        List<AbstractServiceType> serviceTypes = new LinkedList<>();
        try {
            serviceTypes.addAll(getRequiredTopLevelTypeFields(type));
            serviceTypes.addAll(getProvidedTopLevelTypes(type));
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    private List<RequiredServiceType> getRequiredTopLevelTypeFields(final TopLevelType type)
            throws ParseException, JavaModelException {
        List<RequiredServiceType> requiredTypes = new LinkedList<>();
        for (TopLevelType.Field field : type.getFields()) {
            if (Anno2JmlUtil.hasInformationFlowAnnotation(field.getTopLevelType().getIType())) {
                requiredTypes.add(new RequiredServiceType(field.getName(), type, field.getTopLevelType()));
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
    private List<ProvidedServiceType> getProvidedTopLevelTypes(final TopLevelType type)
            throws ParseException, JavaModelException {
        List<TopLevelType> implementedInterfaces = type.getSuperTypeInterfaces();

        List<ProvidedServiceType> providedTypes = new LinkedList<>();
        for (TopLevelType serviceType : implementedInterfaces) {
            if (Anno2JmlUtil.hasInformationFlowAnnotation(serviceType.getIType())) {
                providedTypes.add(new ProvidedServiceType(type, serviceType));
            }
        }
        return providedTypes;
    }
}
