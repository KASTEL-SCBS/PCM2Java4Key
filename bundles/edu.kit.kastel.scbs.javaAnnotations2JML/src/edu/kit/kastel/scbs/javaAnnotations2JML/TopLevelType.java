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

/**
 * Snapshot of an IType from an ICompilationUnit.
 * 
 * For easy access when doing changes at the moment.
 * 
 * Abstraction from multiple possible types in an ICompilationUnit.
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

    public void transformAnnotationsToJml(List<TopLevelType> requiredTopLevelTypes,
            List<TopLevelType> providedTopLevelTypes) throws JavaModelException {
        // TODO WIP
        for (IMethod method : javaType.getMethods()) {
            if (Anno2JmlUtil.hasInformationFlowAnnotation(method)) {
                String jmlComment = JMLCommentsGenerator.toJML(method).toString();
                JdtAstJmlUtil.addStringToMethod(javaType, method, jmlComment);
            }
        }
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

    // TODO not all types at the moment
    public List<IType> getAllIFieldITypes() throws JavaModelException {
        List<IType> allTypes = new LinkedList<>();
        AbstractTypeDeclaration atd = JdtAstJmlUtil
                .findAbstractTypeDeclarationFromIType(getCorrespondingAstCompilationUnit().types(), javaType);
        if (atd instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) atd;
            for (FieldDeclaration fd : td.getFields()) {
                ITypeBinding tb = fd.getType().resolveBinding();
                if (tb.isTopLevel()) {
                    allTypes.add((IType) tb.getJavaElement());
                }
            }
        }
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
}
