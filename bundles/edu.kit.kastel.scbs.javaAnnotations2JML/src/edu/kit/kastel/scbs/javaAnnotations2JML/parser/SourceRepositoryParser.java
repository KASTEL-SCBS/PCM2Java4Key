package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;

public class SourceRepositoryParser extends JavaAnnotations2JMLParser<IJavaProject, List<TopLevelType>> {

    private static final String SOURCE_REPOSITORY = "confidentialityRepository";

    public SourceRepositoryParser(IJavaProject source) {
        super(source);
    }

    @Override
    public List<TopLevelType> parse() {
        List<TopLevelType> topLevelTypes = null; // TODO
        try {
            List<ICompilationUnit> sourceFiles = getSourceFiles();
            topLevelTypes = getTopLevelTypes(sourceFiles);
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setResult(topLevelTypes);
        return getResult();
    }

    private List<ICompilationUnit> getSourceFiles() throws JavaModelException {
        // TODO JavaModelException
        List<ICompilationUnit> sourceFiles = new LinkedList<>();
        IPackageFragment[] fragments = null;
        fragments = getSource().getPackageFragments();

        for (IPackageFragment fragment : fragments) {
            sourceFiles.addAll(Arrays.asList(fragment.getCompilationUnits()));
        }
        return sourceFiles;
    }

    private List<TopLevelType> getTopLevelTypes(List<ICompilationUnit> sourceFiles) throws JavaModelException {
        // TODO JavaModelException
        List<TopLevelType> sourceRepositoryFiles = new LinkedList<>();

        for (ICompilationUnit unit : sourceFiles) {
            List<TopLevelType> tltList = TopLevelType.create(unit);
            sourceRepositoryFiles.addAll(tltList);

            // for (TopLevelType tlt : tltList) {
            // // TODO add if necessary
            // }
        }
        return sourceRepositoryFiles;
    }
}
