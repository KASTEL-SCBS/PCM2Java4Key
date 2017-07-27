package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.List;
import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ConfidentialityRepositoryParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.JavaAnnotations2JMLParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ProjectParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.SourceRepositoryParser;

public class JavaAnnotations2JML {

    private static Optional<JavaAnnotations2JML> singleton = Optional.empty();

    /**
     * No instantiation for singleton class.
     */
    private JavaAnnotations2JML() {
        ////
    }

    public static JavaAnnotations2JML getSingleton() {
        if (!singleton.isPresent()) {
            singleton = Optional.of(new JavaAnnotations2JML());
        }
        return singleton.get();
    }

    public void execute(IProject project) {
        IJavaProject javaProject = new ProjectParser(project).parse();

        SourceRepositoryParser sourceRepoParser = new SourceRepositoryParser(javaProject);
        ConfidentialityRepositoryParser confRepoParser = new ConfidentialityRepositoryParser(javaProject);

        JavaAnnotations2JMLParser<?, ?>[] parsers = { sourceRepoParser, confRepoParser };

        for (JavaAnnotations2JMLParser<?, ?> parser : parsers) {
            parser.parse();
        }
        List<TopLevelType> topLevelTypes = sourceRepoParser.getResult();
        List<DataSet> dataSets = confRepoParser.getResult();

        JMLCommentsGenerator generator = new JMLCommentsGenerator(dataSets, topLevelTypes);
        generator.transformAllAnnotationsToJml();
    }
}
