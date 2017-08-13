package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.kit.kastel.scbs.javaAnnotations2JML.JdtAstJmlUtil;
import edu.kit.kastel.scbs.javaAnnotations2JML.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;

/**
 * Parser for an {@code IJavaProject}. Scans all {@code ICompilationUnit}s,
 * {@code IPackageFragment}s and {@code IType}s of the given project and converts them to
 * {@code TopLevelType}s.
 * 
 * @author Nils Wilka
 * @version 1.0, 04.08.2017
 */
public class TopLevelTypesParser extends JavaAnnotations2JMLParser<List<TopLevelType>, List<TopLevelType>> {

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
        try {
            for (TopLevelType type : getSource()) {
                parseSuperTypeInterfaces(type);
                parseFields(type);
                // only methods of service types are relevant
            }
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
        return getSource();
    }

    public void parseSuperTypeInterfaces(TopLevelType type) throws JavaModelException {
        IType iType = type.getIType();
        List<IType> types = Arrays.asList(iType.newSupertypeHierarchy(null).getAllSuperInterfaces(iType));
        types.forEach(e -> type.addSuperTypeInterface(TopLevelType.create(e)));
    }

    // the java doc of CompilationUnit#types() specifies the type 'AbstractTypeDeclaration'
    @SuppressWarnings("unchecked")
    public void parseFields(TopLevelType type) throws JavaModelException {
        AbstractTypeDeclaration atd = JdtAstJmlUtil.findAbstractTypeDeclarationFromIType(
                type.getCorrespondingAstCompilationUnit().types(), type.getIType());

        List<TopLevelType.Field> allTypes = new LinkedList<>();
        if (atd instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) atd;
            Arrays.asList(td.getFields()).stream().filter(e -> TopLevelType.Field.isTopLevelTypeField(e))
                    .forEach(e -> allTypes.add(TopLevelType.Field.create(type, e)));
        }
        allTypes.forEach(e -> type.addField(e));
    }
}
