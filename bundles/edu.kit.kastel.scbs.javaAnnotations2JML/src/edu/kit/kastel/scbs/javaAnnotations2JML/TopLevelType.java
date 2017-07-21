package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

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

    public void transformAnnotationsToJml() throws JavaModelException {
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
}
