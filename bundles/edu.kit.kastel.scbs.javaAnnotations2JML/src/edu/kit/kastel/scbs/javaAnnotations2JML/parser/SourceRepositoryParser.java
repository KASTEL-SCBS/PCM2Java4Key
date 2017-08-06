package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

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
public class SourceRepositoryParser extends JavaAnnotations2JMLParser<IJavaProject, List<TopLevelType>> {

    /**
     * Constructs a new parser with the given java project as source.
     * 
     * @param source
     *            The {@code IJavaProject} to scan.
     */
    public SourceRepositoryParser(IJavaProject source) {
        super(source);
    }

    @Override
    public List<TopLevelType> parse() throws ParseException {
        List<TopLevelType> topLevelTypes;
        try {
            List<ICompilationUnit> sourceFiles = getSourceFiles();
            topLevelTypes = getTopLevelTypes(sourceFiles);
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
        setResult(topLevelTypes);
        return getResult();
    }

    /**
     * Scans the source {@code IJavaProject} for all java (source) files.
     * 
     * @return A list of source files with possibly multiple {@code IType}s.
     * @throws JavaModelException
     *             if the parsing of the {@code IJavaProject} triggers them.
     */
    private List<ICompilationUnit> getSourceFiles() throws JavaModelException {
        List<ICompilationUnit> sourceFiles = new LinkedList<>();
        IPackageFragment[] fragments = getSource().getPackageFragments();

        for (IPackageFragment fragment : fragments) {
            sourceFiles.addAll(Arrays.asList(fragment.getCompilationUnits()));
        }
        return sourceFiles;
    }

    /**
     * Scans the given source files of type {@code ICompilationUnit} for each {@code IType} (which
     * is normally just one) and creates a {@code TopLevelType} from each.
     * 
     * @param sourceFiles
     *            The source files to scan.
     * @return A list of {@code TopLevelType}s representing all occurring {@code IType}s in the
     *         source java project.
     * @throws JavaModelException
     *             if the parsing of the {@code IJavaProject} triggers them.
     */
    private List<TopLevelType> getTopLevelTypes(List<ICompilationUnit> sourceFiles) throws JavaModelException {
        List<TopLevelType> sourceRepositoryFiles = new LinkedList<>();

        for (ICompilationUnit unit : sourceFiles) {
            List<TopLevelType> tltList = TopLevelType.create(unit);
            sourceRepositoryFiles.addAll(tltList);
        }
        return sourceRepositoryFiles;
    }
}
