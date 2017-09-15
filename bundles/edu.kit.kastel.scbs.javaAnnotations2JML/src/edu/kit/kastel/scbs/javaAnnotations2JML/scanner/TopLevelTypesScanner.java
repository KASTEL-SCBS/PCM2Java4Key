package edu.kit.kastel.scbs.javaAnnotations2JML.scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.JdtAstJmlUtil;

/**
 * Scanner for a list of unique {@code TopLevelType}s. Sets the super types, fields and source
 * methods if necessary.
 * 
 * Ensures that the {@code TopLevelType}s and all references to other {@code TopLevelType}s stay
 * unique.
 * 
 * @author Nils Wilka
 * @version 1.1, 15.09.2017
 */
public class TopLevelTypesScanner {

    private final List<TopLevelType> topLevelTypes;

    private List<TopLevelType> allTopLevelTypes;

    /**
     * Constructs a new scanner with the given list of unique {@code TopLevelType}s.
     * 
     * @param source
     *            The {@code IJavaProject} to scan.
     */
    public TopLevelTypesScanner(List<TopLevelType> source) {
        this.topLevelTypes = source;
    }

    /**
     * Scans the given {@code TopLevelType}s. Sets the super types, fields and source methods if
     * necessary.
     * 
     * @return Returns all top level types in this project and referenced by these.
     * @throws ParseException
     *             if getting the methods from any top level type causes a JavaModelException.
     */
    public List<TopLevelType> scanTopLevelTypes() throws ParseException {
        // TODO overhaul
        allTopLevelTypes = new ArrayList<>(topLevelTypes);
        parseFieldsAndSuperTypes(topLevelTypes);
        List<TopLevelType> usedTypes = new LinkedList<>();
        for (TopLevelType type : allTopLevelTypes) {
            usedTypes.addAll(type.getSuperTypeInterfaces());
            type.getFields().forEach(e -> usedTypes.add(e.getTopLevelType()));
        }
        // used types are to be abstract service types and have relevant methods
        parseMethods(usedTypes);
        return allTopLevelTypes;
    }

    /**
     * Scans the methods of the given {@code TopLevelType}s, i.e. for each type it gets the methods
     * from the associated {@code IType} and adds them as source methods.
     * 
     * @param topLevelTypes
     *            The {@code TopLevelType}s to scan the methods for.
     * @throws ParseException
     *             if getting the methods from the {@code IType} causes a
     *             {@code JavaModelException}.
     */
    private void parseMethods(List<TopLevelType> topLevelTypes) throws ParseException {
        try {
            for (TopLevelType type : topLevelTypes) {
                // set methods for TopLevelTypes instead of getting them from the IType later
                // to avoid JavaModelExceptions outside of parsers
                Arrays.asList(type.getIType().getMethods()).forEach(e -> type.addSourceMethod(e));
            }
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
    }

    /**
     * Scans the fields and super types of the given {@code TopLevelType}s, i.e. for each type it
     * gets the methods from the associated {@code IType} and adds them as source methods.
     * 
     * @param topLevelTypes
     *            The {@code TopLevelType}s to scan the methods for.
     * @throws ParseException
     *             if getting the methods from the {@code IType} causes a
     *             {@code JavaModelException}.
     */
    private void parseFieldsAndSuperTypes(List<TopLevelType> topLevelTypes) throws ParseException {
        try {
            for (TopLevelType type : topLevelTypes) {
                parseSuperTypeInterfaces(type);
                parseFields(type);
            }
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
    }

    /**
     * Scans the super type interfaces for the given type, i.e. creates the super type hierarchy and
     * creates the {@code TopLevelType}s from the {@code IType}s. Then resolves conflicts and adds
     * the newly created types to the given type.
     * 
     * @param type
     *            The type to create and add the super types to.
     * @throws JavaModelException
     *             if creating the super type hierarchy causes it.
     */
    private void parseSuperTypeInterfaces(TopLevelType type) throws JavaModelException {
        IType iType = type.getIType();
        List<IType> iTypes = Arrays.asList(iType.newSupertypeHierarchy(null).getAllSuperInterfaces(iType));
        List<TopLevelType> newTopLevelTypes = iTypes.stream().map(e -> TopLevelType.create(e))
                .collect(Collectors.toList());
        List<TopLevelType> result = replaceExistingTypes(newTopLevelTypes);
        result.forEach(e -> type.addSuperTypeInterface(e));
    }

    /**
     * Scans all fields of the given type, i.e. gets all fields from the associated {@code IType}
     * and creates and adds the {@code TopLevelType.Field}s. Resolves all conflicts of duplicate
     * {@code TopLevelType}s.
     * 
     * @param type
     *            The type to create and add the fields for.
     */
    private void parseFields(TopLevelType type) {
        List<TopLevelType.Field> fields = createTopLevelTypeFields(type);
        List<TopLevelType.Field> result = replaceExistingTypeFields(fields);
        result.forEach(e -> type.addField(e));
    }

    /**
     * The given (new) {@code TopLevelType.Field}s might have duplicate types. Resolves these
     * conflicts by replacing them with their equal types and returning this list.
     * 
     * @param newFields
     *            The top level type fields to check the types for.
     * @return A new list with new fields either with the original type or a replaced one.
     */
    private List<TopLevelType.Field> replaceExistingTypeFields(List<TopLevelType.Field> newFields) {
        List<TopLevelType.Field> result = new ArrayList<>(newFields.size());
        for (TopLevelType.Field field : newFields) {
            result.add(replaceExistingTypeField(field));
        }
        return result;
    }

    /**
     * The given (new) {@code TopLevelType.Field} might have a duplicate type. Resolves this
     * conflict by replacing it with its equal type and returning it.
     * 
     * @param newField
     *            The top level type field to get the original or to add as an original.
     * @return A new field with with either the original type or a replaced one.
     */
    private TopLevelType.Field replaceExistingTypeField(final TopLevelType.Field newField) {
        TopLevelType newType = replaceExistingType(newField.getTopLevelType());
        // TODO this is ... unnecessary
        return new TopLevelType.Field(newField.getParentTopLevelType(), newField.getName(), newType);
    }

    /**
     * Creates a list of {@code TopLevelType.Field}s for the given {@code TopLevelType} according to
     * the fields of the associated {@code IType} and returns it.
     * 
     * @param type
     *            The type to get the fields from.
     * @return The fields of the top level type.
     */
    // the java doc of CompilationUnit#types() specifies the type 'AbstractTypeDeclaration'
    @SuppressWarnings("unchecked")
    private List<TopLevelType.Field> createTopLevelTypeFields(TopLevelType type) {
        AbstractTypeDeclaration atd = JdtAstJmlUtil.findAbstractTypeDeclarationFromIType(
                type.getCorrespondingAstCompilationUnit().types(), type.getIType());
        List<TopLevelType.Field> allTypes = new LinkedList<>();
        if (atd instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) atd;
            // filter non top level type fields
            Arrays.asList(td.getFields()).stream().filter(e -> TopLevelType.Field.isTopLevelTypeField(e))
                    .forEach(e -> allTypes.add(TopLevelType.Field.create(type, e)));
        }
        return allTypes;
    }

    /**
     * The given (new) {@code TopLevelType}s might be duplicates in the global
     * {@code allTopLevelTypes} field or contain duplicates themselves. Resolves these conflicts by
     * replacing them with their equal types and returning this list.
     * 
     * @param newTopLevelTypes
     *            The top level types to get the originals or to add as originals.
     * @return for each type in the given list: if unique the same as the given one, else the
     *         original top level type the given one is equal to.
     */
    private List<TopLevelType> replaceExistingTypes(List<TopLevelType> newTopLevelTypes) {
        List<TopLevelType> result = new ArrayList<>(newTopLevelTypes.size());
        for (TopLevelType newTopLevelType : newTopLevelTypes) {
            result.add(replaceExistingType(newTopLevelType));
        }
        return result;
    }

    /**
     * The given (new) {@code TopLevelType} might be a duplicate in the global
     * {@code allTopLevelTypes} field. If this is the case it is returned, else it is added as a new
     * type to the global field.
     * 
     * @param newTopLevelType
     *            The top level type to get the original or to add as an original.
     * @return if unique the same as the given one, else the original top level type the given one
     *         is equal to.
     */
    private TopLevelType replaceExistingType(final TopLevelType newTopLevelType) {
        TopLevelType result;
        if (allTopLevelTypes.contains(newTopLevelType)) {
            result = allTopLevelTypes.get(allTopLevelTypes.indexOf(newTopLevelType));
        } else {
            allTopLevelTypes.add(newTopLevelType);
            result = newTopLevelType;
        }
        return result;
    }
}
