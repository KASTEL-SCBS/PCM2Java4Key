package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.List;
import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ConfidentialityRepositoryParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.DataSet2AnnotationsParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.JavaAnnotations2JMLParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ProjectParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.RelatedTypesParser;
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
        /* first step: copy and get java project */
        IJavaProject javaProject = new ProjectParser(project).parse();

        /* second step: parse java project for confidentiality package and java types */
        SourceRepositoryParser sourceRepoParser = new SourceRepositoryParser(javaProject);
        ConfidentialityRepositoryParser confRepoParser = new ConfidentialityRepositoryParser(javaProject);

        JavaAnnotations2JMLParser<?, ?>[] parsers = { sourceRepoParser, confRepoParser };

        for (JavaAnnotations2JMLParser<?, ?> parser : parsers) {
            parser.parse();
        }
        List<TopLevelType> topLevelTypes = sourceRepoParser.getResult();
        List<DataSet> dataSets = confRepoParser.getResult();

        /* third step: for each type get required and provided types */
        RelatedTypesParser rtp = new RelatedTypesParser(topLevelTypes);
        TopLevelTypeRelations tltRelations = rtp.parse();

        /*
         * fourth step: for all required and provided types map each data set to its specification
         * elements (methods with annotations) and which type has information about it
         */
        Pair<List<DataSet>, TopLevelTypeRelations> pair = new Pair<>(dataSets, tltRelations);
        DataSet2AnnotationsParser annotationParser = new DataSet2AnnotationsParser(pair);
        annotationParser.parse();

        /* fifth step: generate jml comments for each type and each data set */
        JMLCommentsGenerator generator = new JMLCommentsGenerator(dataSets, tltRelations);
        generator.transformAllAnnotationsToJml();
    }
}
