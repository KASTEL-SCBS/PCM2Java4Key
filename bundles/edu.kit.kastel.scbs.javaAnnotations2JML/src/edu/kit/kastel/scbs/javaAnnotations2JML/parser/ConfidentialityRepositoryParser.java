package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;

import edu.kit.kastel.scbs.javaAnnotations2JML.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.JdtAstJmlUtil;

/**
 * WIP Parser for the confidentiality repository package.
 * 
 * Uses my custom confidentiality specification, until a stable version is available.
 * 
 * @author Nils Wilka
 * @version 0.1
 */
public class ConfidentialityRepositoryParser extends JavaAnnotations2JMLParser<IJavaProject, List<DataSet>> {

    private static final String CONFIDENTIALITY_REPOSITORY = "confidentialityRepository";

    private IPackageFragment confidentialityPackage;

    private IType javaTypeDataSets;

    private IType javaTypeInformationFlow;

    private IType javaTypeParametersAndDataPairs;

    public ConfidentialityRepositoryParser(IJavaProject source) {
        super(source);
        // assert java project has confidentiality repository package
        // assert confidentiality repository package has DataSets.java, InformationFlow.java and
        // ParametersAndDataPairs.java
        // TODO check assertions
    }

    public List<DataSet> parse() {
        try {
            getConfidentialityPackage();
            initializeJavaTypes();
            setResult(getDataSets());
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getResult();
    }

    private void getConfidentialityPackage() throws JavaModelException {
        IPackageFragment[] fragments = getSource().getPackageFragments();
        assert fragments != null && fragments.length > 0;

        for (IPackageFragment fragment : fragments) {
            if (fragment.getElementName().endsWith(CONFIDENTIALITY_REPOSITORY)) {
                confidentialityPackage = fragment;
                return;
            }
        }
        // TODO throw new Exception
    }

    private void initializeJavaTypes() throws JavaModelException {
        // TODO list / array
        javaTypeDataSets = getJavaTypeByName("DataSets");
        assert javaTypeDataSets.isEnum();
        javaTypeInformationFlow = getJavaTypeByName("InformationFlow");
        // javaTypeParametersAndDataPairs = getJavaTypeByName("ParametersAndDataPairs");
    }

    private IType getJavaTypeByName(String name) throws JavaModelException {
        for (ICompilationUnit unit : confidentialityPackage.getCompilationUnits()) {
            assert unit.getTypes() != null && unit.getTypes().length == 1;
            IType type = unit.getTypes()[0];
            if (type.getElementName().equals(name)) {
                return type;
            }
        }
        return null; // TODO throw exception
    }

    private List<DataSet> getDataSets() throws JavaModelException {
        List<DataSet> dataSets = new LinkedList<>();

        CompilationUnit cu = JdtAstJmlUtil.setupParserAndGetCompilationUnit(javaTypeDataSets.getCompilationUnit());
        AbstractTypeDeclaration atd = JdtAstJmlUtil.findAbstractTypeDeclarationFromIType(cu.types(), javaTypeDataSets);
        if (atd instanceof EnumDeclaration) {
            EnumDeclaration ed = (EnumDeclaration) atd;
            for (Object o : ed.enumConstants()) {
                EnumConstantDeclaration etd = (EnumConstantDeclaration) o;
                dataSets.add(DataSet.create(etd));
            }
        }
        return dataSets;
    }
}
