package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * Snapshot of an IType from an ICompilationUnit.
 * 
 * For easy access when doing changes at the moment.
 * 
 * Abstraction from multiple possible types in an ICompilationUnit.
 * 
 * Only for classes and interfaces.
 * 
 * Does not get synchronized with underlying changes.
 * 
 * @author Nils Wilka
 * @version 0.1
 */
public class TopLevelType {

    private Optional<CompilationUnit> astCompilationUnit;

    private ICompilationUnit javaCompilationUnit;

    private IType javaType;

    public TopLevelType(IType type) {
        assert type.getCompilationUnit() != null : "Given type " + type + " not declared in a compilation unit.";
        javaType = type;
        javaCompilationUnit = type.getCompilationUnit();
        astCompilationUnit = Optional.empty();
    }

    public IType getIType() {
        return javaType;
    }

    public ICompilationUnit getCorrespondingJavaCompilationUnit() {
        return javaCompilationUnit;
    }

    public CompilationUnit getCorrespondingAstCompilationUnit() {
        return astCompilationUnit.orElse(setupParserAndCompilationUnit());
    }

    private CompilationUnit setupParserAndCompilationUnit() {
        CompilationUnit cu = JdtAstJmlUtil.setupParserAndGetCompilationUnit(javaCompilationUnit);
        astCompilationUnit = Optional.of(cu);
        return cu;
    }

    public boolean hasInformationFlowAnnotation() throws JavaModelException {
        for (IMethod method : javaType.getMethods()) {
            if (Anno2JmlUtil.hasInformationFlowAnnotation(method)) {
                return true;
            }
        }
        return false;
    }

    public List<IType> getSuperTypeInterfaces() throws JavaModelException {
        return Arrays.asList(javaType.newSupertypeHierarchy(null).getAllSuperInterfaces(javaType));
    }

    public List<IType> getSuperTypeInterfacesWithIFProperty() throws JavaModelException {
        List<IType> interfacesWithIFProperty = new LinkedList<>();
        List<IType> interfaces = getSuperTypeInterfaces();
        for (IType type : interfaces) {
            if (Anno2JmlUtil.hasInformationFlowAnnotation(type)) {
                interfacesWithIFProperty.add(type);
            }
        }
        return interfacesWithIFProperty;
    }

    // the java doc of CompilationUnit#types() specifies the type 'AbstractTypeDeclaration'
    @SuppressWarnings("unchecked")
    public List<TopLevelType.Field> getAllTopLevelTypeFields() throws JavaModelException {
        AbstractTypeDeclaration atd = JdtAstJmlUtil
                .findAbstractTypeDeclarationFromIType(getCorrespondingAstCompilationUnit().types(), javaType);

        List<TopLevelType.Field> allTypes = new LinkedList<>();
        if (atd instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) atd;
            Arrays.asList(td.getFields()).stream().filter(e -> TopLevelType.Field.isTopLevelTypeField(e))
                    .forEach(e -> allTypes.add(TopLevelType.Field.create(this, e)));
        }
        return allTypes; // empty list if not a type declaration
    }

    public static List<TopLevelType> create(ICompilationUnit unit) throws JavaModelException {
        return create(Arrays.asList(unit.getTypes()));
    }

    public static List<TopLevelType> create(List<IType> types) throws JavaModelException {
        List<TopLevelType> tltList = new LinkedList<>();
        for (IType type : types) {
            tltList.add(create(type));
        }
        return tltList;
    }

    public static TopLevelType create(IType type) throws JavaModelException {
        return new TopLevelType(type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof TopLevelType) {
            TopLevelType other = (TopLevelType) obj;
            return this.javaType.equals(other.getIType());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return javaType.hashCode();
    }

    @Override
    public String toString() {
        return javaType.toString();
    }

    public String getName() {
        return javaType.getElementName();
    }

    /**
     * Field of a {@code TopLevelType}.
     * 
     * @author Nils Wilka
     * @version 0.1
     */
    public static class Field {

        private TopLevelType parent;

        private TopLevelType type;

        private String name;

        private Field(final TopLevelType parent, final String name, final TopLevelType type) {
            this.parent = parent;
            this.name = name;
            this.type = type;
        }

        public TopLevelType getParentTopLevelType() {
            return parent;
        }

        public String getName() {
            return name;
        }

        public TopLevelType getTopLevelType() {
            return type;
        }

        public static boolean isTopLevelTypeField(FieldDeclaration fieldDeclaration) {
            return fieldDeclaration.getType().resolveBinding().isTopLevel();
        }

        public static Field create(final TopLevelType parent, FieldDeclaration fieldDeclaration) {
            ITypeBinding tb = fieldDeclaration.getType().resolveBinding();
            if (tb.isTopLevel()) {
                VariableDeclarationFragment vdf = (VariableDeclarationFragment) fieldDeclaration.fragments().get(0);
                IType type = (IType) tb.getJavaElement();
                return new Field(parent, vdf.toString(), new TopLevelType(type));
            } else {
                // TODO throw exception ?
                return null;
            }
        }

        public static List<Field> create(final TopLevelType parent, List<FieldDeclaration> fdList)
                throws JavaModelException {
            List<Field> fields = new LinkedList<>();
            fdList.forEach(e -> fields.add(create(parent, e)));
            return fields;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
