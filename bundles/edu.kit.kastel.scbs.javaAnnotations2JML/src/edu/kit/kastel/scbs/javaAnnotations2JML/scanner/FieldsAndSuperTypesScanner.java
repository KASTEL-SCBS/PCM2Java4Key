package edu.kit.kastel.scbs.javaAnnotations2JML.scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.JdtAstJmlUtil;

/**
 * Scanner for fields and super types of ITypes. Creates the corresponding
 * {@code TopLevelType.Field}s and {@code TopLevelType.SuperType}s.
 * 
 * Ensures that all references to {@code IType}s stay unique.
 * 
 * @author Nils Wilka
 * @version 1.2, 16.09.2017
 */
public class FieldsAndSuperTypesScanner {

    private final Iterable<TopLevelType> topLevelTypes;

    private final List<IType> types;

    /**
     * Constructs a new scanner with the given iterable of unique {@code TopLevelType}s to scan the
     * super types and fields for.
     * 
     * @param source
     *            The iterable of unique {@code TopLevelType}s to scan the super types and fields
     *            for.
     */
    public FieldsAndSuperTypesScanner(final Iterable<TopLevelType> source) {
        this.topLevelTypes = source;
        this.types = new LinkedList<>();
    }

    /**
     * Scans the fields and super types for the {@code TopLevelType}s by accessing the IType.
     * 
     * @throws ParseException
     *             if creating the super type hierarchy for the top level type causes it.
     */
    public void scan() throws ParseException {
        topLevelTypes.forEach(e -> types.add(e.getIType()));
        try {
            for (TopLevelType type : topLevelTypes) {
                scanSuperTypes(type);
                scanFields(type);
            }
        } catch (JavaModelException jme) {
            final Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
    }

    /**
     * Scans the super type interfaces for the given type, i.e. creates the super type hierarchy and
     * creates the {@code TopLevelType.SuperType}s from the {@code IType}. Then resolves conflicts
     * and adds the newly created types to the given type.
     * 
     * @param type
     *            The type to create the super types for and add the super types to.
     * @throws JavaModelException
     *             if creating the super type hierarchy causes it.
     */
    private void scanSuperTypes(final TopLevelType type) throws JavaModelException {
        final IType iType = type.getIType();
        List<IType> iTypes = Arrays.asList(iType.newSupertypeHierarchy(null).getSupertypes(iType));
        iTypes = replaceExistingTypes(iTypes);
        iTypes.stream().forEach(e -> type.addSuperType(new TopLevelType.SuperType(type, e)));
    }

    /**
     * Scans all fields of the given type, i.e. gets all fields from the associated {@code IType}
     * and creates and adds the {@code TopLevelType.Field}s. Resolves all conflicts of duplicate
     * {@code TopLevelType}s.
     * 
     * @param type
     *            The type to create and add the fields for.
     */
    private void scanFields(final TopLevelType type) {
        final List<TopLevelType.Field> fields = createTopLevelTypeFields(type);
        final List<TopLevelType.Field> result = replaceExistingTypeFields(fields);
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
    private List<TopLevelType.Field> replaceExistingTypeFields(final List<TopLevelType.Field> newFields) {
        final List<TopLevelType.Field> result = new ArrayList<>(newFields.size());
        newFields.forEach(e -> result.add(replaceExistingTypeField(e)));
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
        final IType newType = replaceExistingType(newField.getType());
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
    private List<TopLevelType.Field> createTopLevelTypeFields(final TopLevelType type) {
        final AbstractTypeDeclaration atd = JdtAstJmlUtil.findAbstractTypeDeclarationFromIType(
                type.getCorrespondingAstCompilationUnit().types(), type.getIType());
        final List<TopLevelType.Field> allFields = new LinkedList<>();
        if (atd instanceof TypeDeclaration) {
            final TypeDeclaration td = (TypeDeclaration) atd;
            // filter non top level type fields
            Arrays.asList(td.getFields()).stream().filter(e -> TopLevelType.Field.isTopLevelTypeField(e))
                    .forEach(e -> allFields.addAll(TopLevelType.Field.create(type, e)));
        }
        return allFields;
    }

    /**
     * The given (new) {@code IType}s might be duplicates in the global {@code types} field or
     * contain duplicates themselves. Resolves these conflicts by replacing them with their equal
     * types and returning this list.
     * 
     * @param newTypes
     *            The types to get the originals for or to add as originals.
     * @return for each type in the given list: if unique the same as the given one, else the
     *         original type the given one is equal to.
     */
    private List<IType> replaceExistingTypes(final List<IType> newTypes) {
        final List<IType> result = new ArrayList<>(newTypes.size());
        for (IType type : newTypes) {
            result.add(replaceExistingType(type));
        }
        return result;
    }

    /**
     * The given (new) {@code IType} might be a duplicate in the global {@code types} field. If this
     * is the case the already existing type is returned, else the new types is added to the global
     * field.
     * 
     * @param type
     *            The possibly duplicate type to get the original for or to add as an original.
     * @return if unique the same as the given one, else the original type the given one is equal
     *         to.
     */
    private IType replaceExistingType(final IType type) {
        final IType result;
        if (types.contains(type)) {
            result = types.get(types.indexOf(type));
        } else {
            types.add(type);
            result = type;
        }
        return result;
    }
}
