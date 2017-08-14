package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

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
 * Parser for an {@code IJavaProject}. Scans all {@code ICompilationUnit}s,
 * {@code IPackageFragment}s and {@code IType}s of the given project and converts them to
 * {@code TopLevelType}s.
 * 
 * @author Nils Wilka
 * @version 1.0, 04.08.2017
 */
public class TopLevelTypesParser extends JavaAnnotations2JMLParser<List<TopLevelType>, List<TopLevelType>> {

    // copy
    private List<TopLevelType> allTopLevelTypes;

    /**
     * Constructs a new parser with the given java project as source.
     * 
     * @param source
     *            The {@code IJavaProject} to scan.
     */
    public TopLevelTypesParser(List<TopLevelType> source) {
        super(source);
    }

    @Override
    protected List<TopLevelType> parseSource() throws ParseException {
        allTopLevelTypes = new ArrayList<>(getSource());
        parseFieldsAndSuperTypes(getSource());
        List<TopLevelType> usedTypes = new LinkedList<>();
        for (TopLevelType type : allTopLevelTypes) {
            usedTypes.addAll(type.getSuperTypeInterfaces());
            type.getFields().forEach(e -> usedTypes.add(e.getTopLevelType()));
        }
        parseMethods(usedTypes);
        return allTopLevelTypes;
    }

    private void parseMethods(List<TopLevelType> topLevelTypes) throws ParseException {
        try {
            for (TopLevelType type : topLevelTypes) {
                Arrays.asList(type.getIType().getMethods()).forEach(e -> type.addSourceMethod(e));
            }
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
    }

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

    private void parseSuperTypeInterfaces(TopLevelType type) throws JavaModelException {
        IType iType = type.getIType();
        List<IType> iTypes = Arrays.asList(iType.newSupertypeHierarchy(null).getAllSuperInterfaces(iType));
        List<TopLevelType> newTopLevelTypes = iTypes.stream().map(e -> TopLevelType.create(e))
                .collect(Collectors.toList());
        List<TopLevelType> result = replaceExistingTypes(newTopLevelTypes);
        result.forEach(e -> type.addSuperTypeInterface(e));
    }

    private void parseFields(TopLevelType type) throws JavaModelException {
        List<TopLevelType.Field> fields = createTopLevelTypeFields(type);
        List<TopLevelType.Field> result = replaceExistingTypeFields(fields);
        result.forEach(e -> type.addField(e));
    }

    private List<TopLevelType.Field> replaceExistingTypeFields(List<TopLevelType.Field> newFields)
            throws JavaModelException {
        List<TopLevelType.Field> result = new ArrayList<>(newFields.size());
        for (TopLevelType.Field field : newFields) {
            result.add(replaceExistingTypeField(field));
        }
        return result;
    }

    private TopLevelType.Field replaceExistingTypeField(final TopLevelType.Field newField) throws JavaModelException {
        TopLevelType newType = replaceExistingType(newField.getTopLevelType());
        return new TopLevelType.Field(newField.getParentTopLevelType(), newField.getName(), newType);
    }

    // the java doc of CompilationUnit#types() specifies the type 'AbstractTypeDeclaration'
    @SuppressWarnings("unchecked")
    private List<TopLevelType.Field> createTopLevelTypeFields(TopLevelType type) throws JavaModelException {
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

    private List<TopLevelType> replaceExistingTypes(List<TopLevelType> newTopLevelTypes) throws JavaModelException {
        List<TopLevelType> result = new ArrayList<>(newTopLevelTypes.size());
        for (TopLevelType newTopLevelType : newTopLevelTypes) {
            result.add(replaceExistingType(newTopLevelType));
        }
        return result;
    }

    private TopLevelType replaceExistingType(final TopLevelType newTopLevelType) throws JavaModelException {
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
