package edu.kit.kastel.scbs.javaAnnotations2JML.transformer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Scanner for the given {@code IJavaProject} and to extract the {@code TopLevelType}s from the
 * contained source files. Scans all {@code ICompilationUnit}s, {@code IPackageFragment}s and
 * {@code IType}s of the given project and converts them to {@code TopLevelType}s.
 * 
 * @author Nils Wilka
 * @version 1.1, 16.09.2017
 */
public class SourceFilesToTopLevelTypesTransformer {

    private final IJavaProject project;

    /**
     * Constructs a new scanner with the given java project as source.
     * 
     * @param source
     *            The {@code IJavaProject} to scan.
     */
    public SourceFilesToTopLevelTypesTransformer(final IJavaProject source) {
        this.project = source;
    }

    /**
     * Scans all {@code ICompilationUnit}s, {@code IPackageFragment}s and {@code IType}s of the
     * given project and converts them to {@code TopLevelType}s.
     * 
     * @return The top level types corresponding to the ITypes in the project;
     * @throws ParseException
     *             if the parsing of the java project triggers it.
     */
    public Iterable<TopLevelType> transform() throws ParseException {
        final Iterable<TopLevelType> topLevelTypes;
        try {
            final Iterable<ICompilationUnit> sourceFiles = getSourceFiles();
            topLevelTypes = getTopLevelTypes(sourceFiles);
        } catch (JavaModelException jme) {
            final Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
        return topLevelTypes;
    }

    /**
     * Scans the source {@code IJavaProject} for all java (source) files.
     * 
     * @return A list of source files with possibly multiple {@code IType}s.
     * @throws JavaModelException
     *             if the parsing of the {@code IJavaProject} triggers them.
     */
    private Iterable<ICompilationUnit> getSourceFiles() throws JavaModelException {
        final Set<ICompilationUnit> sourceFiles = new HashSet<>();
        final IPackageFragment[] fragments = project.getPackageFragments();
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
    private Iterable<TopLevelType> getTopLevelTypes(final Iterable<ICompilationUnit> sourceFiles)
            throws JavaModelException {
        final Set<TopLevelType> sourceRepositoryFiles = new HashSet<>();
        for (ICompilationUnit unit : sourceFiles) {
            final List<TopLevelType> tltList = TopLevelType.create(Arrays.asList(unit.getTypes()));
            sourceRepositoryFiles.addAll(tltList);
        }
        return sourceRepositoryFiles;
    }
}
