package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.Anno2JmlUtil;
import edu.kit.kastel.scbs.javaAnnotations2JML.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelTypeMappings;

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
public class ServiceParser extends JavaAnnotations2JMLParser<List<TopLevelType>, TopLevelTypeMappings> {

    /**
     * Constructs a new parser with the given ist of {@code TopLevelType}s.
     * 
     * @param source
     *            The list of {@code TopLevelType}s to scan.
     */
    public ServiceParser(List<TopLevelType> source) {
        super(source);
    }

    @Override
    public TopLevelTypeMappings parse() throws ParseException {
        TopLevelTypeMappings tltMappings = new TopLevelTypeMappings(getSource());

        for (TopLevelType type : getSource()) {
            List<TopLevelType.Field> requiredTopLevelTypeFields;
            List<TopLevelType> providedTopLevelTypes;
            try {
                requiredTopLevelTypeFields = getRequiredTopLevelTypeFields(type);
                providedTopLevelTypes = getProvidedTopLevelTypes(type);
            } catch (JavaModelException jme) {
                Optional<String> message = Optional.ofNullable(jme.getMessage());
                throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
            }
            if (!providedTopLevelTypes.isEmpty()) {
                tltMappings.addProvidedTopLevelTypes(type, providedTopLevelTypes);
            }
            if (!requiredTopLevelTypeFields.isEmpty()) {
                tltMappings.addRequiredTopLevelTypeFields(type, requiredTopLevelTypeFields);
            }
        }
        setResult(tltMappings);
        return getResult();
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
    private List<TopLevelType.Field> getRequiredTopLevelTypeFields(final TopLevelType type) throws JavaModelException {
        List<TopLevelType.Field> fieldTypes = type.getAllTopLevelTypeFields();
        List<TopLevelType.Field> fieldsWithIFProperty = new LinkedList<>();
        for (TopLevelType.Field field : fieldTypes) {
            if (Anno2JmlUtil.hasInformationFlowAnnotation(field.getTopLevelType().getIType())) {
                // if its an top level type and has information flow annotations
                // then it is required.
                fieldsWithIFProperty.add(field);
            }
        }
        return fieldsWithIFProperty;
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
    private List<TopLevelType> getProvidedTopLevelTypes(final TopLevelType type) throws JavaModelException {
        List<IType> implementedInterfaces = type.getSuperTypeInterfacesWithIFProperty();
        return TopLevelType.create(implementedInterfaces);
    }
}
