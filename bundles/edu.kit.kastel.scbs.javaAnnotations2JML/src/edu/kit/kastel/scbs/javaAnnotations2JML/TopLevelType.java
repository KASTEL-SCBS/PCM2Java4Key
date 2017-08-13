package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.AbstractServiceType;

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
public class TopLevelType implements MethodProvider {

    private Optional<CompilationUnit> astCompilationUnit;

    private ICompilationUnit javaCompilationUnit;

    private IType javaType;

    private List<TopLevelType> superInterfaces;

    private List<TopLevelType.Field> fields;

    private List<AbstractServiceType> serviceTypes;

    private Set<DataSet> dataSets;

    private Map<IMethod, InformationFlowAnnotation> methods;

    public TopLevelType(IType type) {
        // assert type.getCompilationUnit() != null : "Given type " + type + " not declared in a
        // compilation unit.";
        javaType = type;
        javaCompilationUnit = type.getCompilationUnit();
        astCompilationUnit = Optional.empty();
        superInterfaces = new LinkedList<>();
        fields = new LinkedList<>();
        serviceTypes = new LinkedList<>();
        dataSets = new HashSet<>();
        methods = new HashMap<>();
    }

    public List<AbstractServiceType> getServiceTypes() {
        Optional<List<AbstractServiceType>> optional = Optional.ofNullable(serviceTypes);
        return optional.orElse(new LinkedList<>());
    }

    public void addServiceTypes(List<AbstractServiceType> serviceTypes) {
        serviceTypes.forEach(e -> addServiceType(e));
    }

    public void addServiceType(AbstractServiceType serviceType) {
        this.serviceTypes.add(serviceType);
        // TODO service provider does not yet have services
        serviceType.getServiceProvider().getDataSets().forEach(e -> addDataSet(e));
    }

    public String getName() {
        return javaType.getElementName();
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

    public List<TopLevelType> getSuperTypeInterfaces() {
        Optional<List<TopLevelType>> optional = Optional.ofNullable(superInterfaces);
        return optional.orElse(new LinkedList<>());
    }

    public void addSuperTypeInterface(TopLevelType superTypeInterface) {
        superInterfaces.add(superTypeInterface);
    }

    public List<TopLevelType.Field> getFields() {
        Optional<List<TopLevelType.Field>> optional = Optional.ofNullable(fields);
        return optional.orElse(new LinkedList<>());
    }

    public void addField(TopLevelType.Field field) {
        fields.add(field);
    }

    public Set<DataSet> getDataSets() {
        return dataSets;
    }

    public void addDataSet(DataSet dataSet) {
        // type -> data set
        this.dataSets.add(dataSet);
        // data set -> type
        dataSet.addTopLevelType(this);
    }

    public void addMethod(IMethod method, InformationFlowAnnotation annotation) {
        methods.put(method, annotation);
    }

    @Override
    public Map<IMethod, InformationFlowAnnotation> getMethods() {
        return methods;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(javaType, obj);
    }

    @Override
    public int hashCode() {
        return javaType.hashCode();
    }

    @Override
    public String toString() {
        return javaType.toString();
    }

    public static List<TopLevelType> create(List<IType> types) {
        return types.stream().map(e -> create(e)).collect(Collectors.toList());
    }

    public static TopLevelType create(IType type) {
        return new TopLevelType(type);
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
