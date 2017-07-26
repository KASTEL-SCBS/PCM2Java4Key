package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMemberValuePair;
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

    private Optional<CompilationUnit> astCompilationUnit = Optional.empty();

    private ICompilationUnit javaCompilationUnit;

    private IType javaType;

    public TopLevelType(IType type) throws JavaModelException {
        // TODO IndexOutOfBoundsException
        javaCompilationUnit = type.getCompilationUnit();
        javaType = type;
    }

    public TopLevelType(ICompilationUnit unit, int index) throws JavaModelException {
        // TODO IndexOutOfBoundsException
        // TODO JavaModelException
        javaCompilationUnit = unit;
        javaType = javaCompilationUnit.getTypes()[index];
    }

    public IType getIType() {
        return javaType;
    }

    public ICompilationUnit getCorrespondingJavaCompilationUnit() {
        return javaCompilationUnit;
    }

    public CompilationUnit getCorrespondingAstCompilationUnit() {
        if (!astCompilationUnit.isPresent()) {
            setupParserAndCompilationUnit();
        }
        return astCompilationUnit.get();
    }

    private void setupParserAndCompilationUnit() {
        astCompilationUnit = Optional.of(JdtAstJmlUtil.setupParserAndGetCompilationUnit(javaCompilationUnit));
    }

    public void transformAnnotationsToJml(List<DataSet> datSets) throws JavaModelException {
        List<TopLevelType.Field> requiredTopLevelTypeFields = getRequiredTopLevelTypeFields();
        List<TopLevelType> providedTopLevelTypes = getProvidedTopLevelTypes();

        if (!requiredTopLevelTypeFields.isEmpty() || !providedTopLevelTypes.isEmpty()) {
            // TODO WIP
            for (DataSet dataSet : datSets) {
                JmlComment comment = new JmlComment(dataSet);

                for (TopLevelType.Field field : requiredTopLevelTypeFields) {
                    TopLevelType fieldType = field.getTopLevelType();
                    HashMap<IMethod, String> method2ParameterSourcesMap = fieldType
                            .getMethodParameterSourcesPairs(dataSet);
                    for (IMethod method : method2ParameterSourcesMap.keySet()) {
                        String role = field.getName();
                        String service = method.getElementName();
                        String parameterSources = method2ParameterSourcesMap.get(method);
                        comment.addDeterminesLine(role, service, parameterSources);
                    }
                }

                for (TopLevelType type : providedTopLevelTypes) {
                    HashMap<IMethod, String> method2ParameterSourcesMap = type.getMethodParameterSourcesPairs(dataSet);
                    for (IMethod method : method2ParameterSourcesMap.keySet()) {
                        String role = "this";
                        String service = method.getElementName();
                        String parameterSources = method2ParameterSourcesMap.get(method);
                        comment.addDeterminesLine(role, service, parameterSources);
                    }
                }

                JdtAstJmlUtil.addStringToAbstractType(javaType, comment.toString());
            }
        }
    }

    // TODO rename
    public HashMap<IMethod, String> getMethodParameterSourcesPairs(DataSet dataSet) throws JavaModelException {
        HashMap<IMethod, String> method2ParameterSourcesMap = new HashMap<>();

        for (IMethod method : javaType.getMethods()) {
            String parameterSources = getMethodParameterSourcesPair(dataSet, method);
            method2ParameterSourcesMap.put(method, parameterSources);
        }
        return method2ParameterSourcesMap;
    }

    // TODO rename
    public String getMethodParameterSourcesPair(DataSet dataSet, IMethod method) throws JavaModelException {
        String parameterSources = "";

        IMemberValuePair[] pair = Anno2JmlUtil.getIFAnnotationArguments(method);
        if (pair != null && pair.length > 0) {
            String dataSetName = pair[1].getValue().toString(); // TODO does not work
            DataSet currentDataSet = new DataSet(dataSetName);
            if (currentDataSet.equals(dataSet)) {
                // this annotation has relevant information
                parameterSources = pair[0].getValue().toString(); // TODO does not work
                // TODO parse parameterSources
                // TODO add parameters
            }
        } // else nothing specified
        return parameterSources;
    }

    public boolean hasInformationFlowAnnotation() throws JavaModelException {
        for (IMethod method : javaType.getMethods()) {
            if (Anno2JmlUtil.hasInformationFlowAnnotation(method)) {
                return true;
            }
        }
        return false;
    }

    private List<TopLevelType.Field> getRequiredTopLevelTypeFields() throws JavaModelException {
        List<TopLevelType.Field> fieldTypes = getAllTopLevelTypeFields();
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

    private List<TopLevelType> getProvidedTopLevelTypes() throws JavaModelException {
        List<IType> implementedInterfaces = getSuperTypeInterfacesWithIFProperty();
        return TopLevelType.create(implementedInterfaces);
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

    public List<TopLevelType.Field> getAllTopLevelTypeFields() throws JavaModelException {
        List<TopLevelType.Field> allTypes = new LinkedList<>();
        AbstractTypeDeclaration atd = JdtAstJmlUtil
                .findAbstractTypeDeclarationFromIType(getCorrespondingAstCompilationUnit().types(), javaType);
        if (atd instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) atd;
            for (FieldDeclaration fd : td.getFields()) {
                if (TopLevelType.Field.isTopLevelTypeField(fd)) {
                    allTypes.add(TopLevelType.Field.create(this, fd));
                }
            }
        }
        // TODO else
        return allTypes;
    }

    public static List<TopLevelType> create(ICompilationUnit unit) throws JavaModelException {
        // TODO IndexOutOfBoundsException
        // TODO JavaModelException
        List<TopLevelType> tltList = new LinkedList<>();
        for (IType type : unit.getTypes()) {
            TopLevelType tlt = new TopLevelType(type);
            tltList.add(tlt);
        }
        return tltList;
    }

    public static TopLevelType create(IType type) throws JavaModelException {
        return new TopLevelType(type);
    }

    public static List<TopLevelType> create(List<IType> types) throws JavaModelException {
        // TODO IndexOutOfBoundsException
        // TODO JavaModelException
        List<TopLevelType> tltList = new LinkedList<>();
        for (IType type : types) {
            TopLevelType tlt = new TopLevelType(type);
            tltList.add(tlt);
        }
        return tltList;
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

        private Field(final TopLevelType parent, final String name, final TopLevelType type) throws JavaModelException {
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
            ITypeBinding tb = fieldDeclaration.getType().resolveBinding();
            return tb.isTopLevel();
        }

        public static Field create(final TopLevelType parent, FieldDeclaration fieldDeclaration)
                throws JavaModelException {
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
            for (FieldDeclaration fieldDeclaration : fdList) {
                ITypeBinding tb = fieldDeclaration.getType().resolveBinding();
                if (tb.isTopLevel()) {
                    VariableDeclarationFragment vdf = (VariableDeclarationFragment) fieldDeclaration.fragments().get(0);
                    IType type = (IType) tb.getJavaElement();
                    fields.add(new Field(parent, vdf.toString(), new TopLevelType(type)));
                } else {
                    // TODO
                }
            }
            return fields;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
