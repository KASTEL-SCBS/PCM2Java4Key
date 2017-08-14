package edu.kit.kastel.scbs.javaAnnotations2JML.type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.JdtAstJmlUtil;

/**
 * Wrapper for an {@code IType} from an either an {@code ICompilationUnit} or a binary class. Does
 * not parse its given {@code IType} and therefore does not provide direct access to the values of
 * its {@code IType}. Its {@code IType} can nevertheless be accessed by
 * {@code TopLevelType#getIType}.
 * 
 * The values of the {@code IType} must be parsed and added to this type, before they can be used.
 * This includes the source {@code IMethod}s, {@code TopLevelType.Field}s, super
 * {@code TopLevelType}s, parsed {@code IMethod}s with their {@code InformationFlowAnnotation}s and
 * lastly the {@code AbstractServiceType}s.
 * 
 * A {@code TopLevelType} should only consist of values from its corresponding {@code IType}.
 * 
 * @see IType
 * @see ICompilationUnit
 * @see IMethod
 * @see CompilationUnit
 * @see AbstractServiceType
 * @see InformationFlowAnnotation
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public class TopLevelType implements MethodAcceptor, MethodProvider, SourceMethodAcceptor, SourceMethodProvider {

    private Optional<CompilationUnit> astCompilationUnit;

    private ICompilationUnit javaCompilationUnit;

    private IType javaType;

    private List<TopLevelType> superInterfaces;

    private List<TopLevelType.Field> fields;

    private List<AbstractServiceType> serviceTypes;

    private List<IMethod> sourceMethods;

    private Map<IMethod, InformationFlowAnnotation> methods;

    /**
     * Creates a Wrapper for an {@code IType} from an either an {@code ICompilationUnit} or a binary
     * class. Keep in mind, that it does not parse its given {@code IType} and therefore does not
     * provide direct access to the values of its {@code IType}. They must be manually added first.
     * 
     * @param type
     *            The wrapped {@code IType} from an either an {@code ICompilationUnit} or a binary
     *            class.
     */
    public TopLevelType(IType type) {
        javaType = type;
        javaCompilationUnit = type.getCompilationUnit();
        astCompilationUnit = Optional.empty();
        superInterfaces = new LinkedList<>();
        fields = new LinkedList<>();
        serviceTypes = new LinkedList<>();
        methods = new HashMap<>();
        sourceMethods = new LinkedList<>();
    }

    /**
     * Gets the {@code AbstractServiceType}s of this {@code TopLevelType}. Service types include all
     * super types and fields, which contain an information flow annotation and are either 'Required
     * Types' or 'Provided Types'
     * 
     * @return The {@code AbstractServiceType}s of this {@code TopLevelType}
     * 
     * @see AbstractServiceType
     */
    public List<AbstractServiceType> getServiceTypes() {
        Optional<List<AbstractServiceType>> optional = Optional.ofNullable(serviceTypes);
        return optional.orElse(new LinkedList<>());
    }

    /**
     * Adds new {@code AbstractServiceType}s to this {@code TopLevelType}. These types should
     * correspond to the super types and fields of this top level type.
     * 
     * @param serviceTypes
     *            The new {@code AbstractServiceType}s to be added.
     * 
     * @see AbstractServiceType
     */
    public void addServiceTypes(List<AbstractServiceType> serviceTypes) {
        serviceTypes.forEach(e -> addServiceType(e));
    }

    /**
     * Adds a new {@code AbstractServiceType} to this {@code TopLevelType}. This type should
     * correspond to a super type or a field of this top level type.
     * 
     * @param serviceType
     *            The new {@code AbstractServiceType} to be added.
     * 
     * @see AbstractServiceType
     */
    public void addServiceType(AbstractServiceType serviceType) {
        this.serviceTypes.add(serviceType);
    }

    /**
     * Gets the name of this {@code TopLevelType}, which is exactly the same as the one from its
     * {@code IType}.
     * 
     * @return The name of this {@code TopLevelType}.
     */
    public String getName() {
        return javaType.getElementName();
    }

    /**
     * Gets the wrapped {@code IType}, e.g. for parsing purposes. This class should only provide
     * information about this specific {@code IType}.
     * 
     * @return The wrapped {@code IType}.
     */
    public IType getIType() {
        return javaType;
    }

    /**
     * Gets the Java {@code CompilationUnit} (AST node type) corresponding to the {@code IType} and
     * therefore this {@code TopLevelType}.
     * 
     * @return The Java {@code CompilationUnit} corresponding to this {@code TopLevelType}.
     * 
     * @see CompilationUnit
     */
    public CompilationUnit getCorrespondingAstCompilationUnit() {
        return astCompilationUnit.orElse(setupParserAndCompilationUnit());
    }

    /**
     * Creates a new {@code ASTParser} and with its help the Java {@code CompilationUnit} (AST node
     * type) corresponding to the {@code IType} and therefore this {@code TopLevelType}.
     * 
     * @return The Java {@code CompilationUnit} corresponding to this {@code TopLevelType}.
     */
    private CompilationUnit setupParserAndCompilationUnit() {
        CompilationUnit cu = JdtAstJmlUtil.setupParserAndGetCompilationUnit(javaCompilationUnit);
        astCompilationUnit = Optional.of(cu);
        return cu;
    }

    /**
     * Gets the super type interfaces for this {@code TopLevelType}.
     * 
     * @return The super type interfaces for this {@code TopLevelType}.
     */
    public List<TopLevelType> getSuperTypeInterfaces() {
        Optional<List<TopLevelType>> optional = Optional.ofNullable(superInterfaces);
        return optional.orElse(new LinkedList<>());
    }

    /**
     * Adds a super type interface to this {@code TopLevelType}.
     * 
     * @param superTypeInterface
     *            The super type interfaces to add.
     */
    public void addSuperTypeInterface(TopLevelType superTypeInterface) {
        superInterfaces.add(superTypeInterface);
    }

    /**
     * Gets the fields for this {@code TopLevelType}.
     * 
     * @return The fields for this {@code TopLevelType}.
     */
    public List<TopLevelType.Field> getFields() {
        Optional<List<TopLevelType.Field>> optional = Optional.ofNullable(fields);
        return optional.orElse(new LinkedList<>());
    }

    /**
     * Adds a field to this {@code TopLevelType}.
     * 
     * @param field
     *            The field to add.
     */
    public void addField(TopLevelType.Field field) {
        fields.add(field);
    }

    /**
     * Gets the {@code DataSet}s from all {@code AbstractServiceType}s for this
     * {@code TopLevelType}.
     * 
     * @return The related {@code DataSet}s for this {@code TopLevelType}.
     */
    public Set<DataSet> getServiceTypeDataSets() {
        Set<DataSet> dataSets = new HashSet<>();
        serviceTypes.forEach(e -> dataSets.addAll(e.getDataSets()));
        return dataSets;
    }

    @Override
    public void addMethod(IMethod method, InformationFlowAnnotation annotation) {
        methods.put(method, annotation);
    }

    @Override
    public Map<IMethod, InformationFlowAnnotation> getMethods() {
        return methods;
    }

    @Override
    public void addSourceMethod(IMethod method) {
        this.sourceMethods.add(method);
    }

    @Override
    public List<IMethod> getSourceMethods() throws JavaModelException {
        return Arrays.asList(this.getIType().getMethods());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof TopLevelType) {
            TopLevelType other = (TopLevelType) obj;
            // equal by name
            return this.javaType.getElementName().equals(other.javaType.getElementName());
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

    /**
     * Creates multiple {@code TopLevelType}s with the given {@code IType}s.
     * 
     * @param types
     *            The {@code IType}s to create each {@code TopLevelType} from.
     * @return The newly created top level types.
     */
    public static List<TopLevelType> create(List<IType> types) {
        return types.stream().map(e -> create(e)).collect(Collectors.toList());
    }

    /**
     * Create a new {@code TopLevelType} with the given {@code IType}.
     * 
     * @param type
     *            The {@code IType} to create the {@code TopLevelType} from.
     * @return The newly created top level type.
     */
    public static TopLevelType create(IType type) {
        return new TopLevelType(type);
    }

    /**
     * Field of a {@code TopLevelType} corresponding to a field of an {@code IType}.
     * 
     * @author Nils Wilka
     * @version 1.0, 14.08.2017
     */
    public static class Field {

        private TopLevelType parent;

        private TopLevelType type;

        private String name;

        /**
         * Creates a new top level type field for the {@code parent} with its {@code type} and
         * {@code name}.
         * 
         * @param parent
         *            Where the field is declared.
         * @param name
         *            The name of the field.
         * @param type
         *            The type of the field.
         */
        public Field(final TopLevelType parent, final String name, final TopLevelType type) {
            this.parent = parent;
            this.name = name;
            this.type = type;
        }

        /**
         * Gets the parent of this field - the place where it is declared.
         * 
         * @return The parent of this field.
         */
        public TopLevelType getParentTopLevelType() {
            return parent;
        }

        /**
         * Gets the name of this field.
         * 
         * @return The name of this field.
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the type of this field.
         * 
         * @return The type of this field.
         */
        public TopLevelType getTopLevelType() {
            return type;
        }

        /**
         * Checks whether the type of the given {@code FieldDeclaration} is a top level type.
         * 
         * @param fieldDeclaration
         *            The {@code FieldDeclaration} to check.
         * @return true if this field has a top level type or false if not.
         */
        public static boolean isTopLevelTypeField(FieldDeclaration fieldDeclaration) {
            return fieldDeclaration.getType().resolveBinding().isTopLevel();
        }

        /**
         * Creates a new field for a {@code TopLevelType} from the given {@code FieldDeclaration}.
         * 
         * The {@code TopLevelType} of this field will be created.
         * 
         * @param parent
         *            Where the field is declared.
         * @param fieldDeclaration
         *            To get the field values from.
         * @return A new field for the parent.
         */
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

        /**
         * Creates a new list of fields for the parent from the given {@code FieldDeclaration}s.
         * 
         * The {@code TopLevelType} of these fields will be created individually.
         * 
         * @param parent
         *            Where the field is declared.
         * @param fdList
         *            To get the field values from.
         * @return A new list of fields for the parent.
         */
        public static List<Field> create(final TopLevelType parent, List<FieldDeclaration> fdList) {
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
