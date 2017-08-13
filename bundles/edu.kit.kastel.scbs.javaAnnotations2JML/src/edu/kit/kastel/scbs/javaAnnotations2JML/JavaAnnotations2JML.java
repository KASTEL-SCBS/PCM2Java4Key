package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.JMLCommentsGenerator;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ConfidentialityRepositoryParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.JavaAnnotations2JMLParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.JavaProjectParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.MethodParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ProjectParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ServiceTypeParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.TopLevelTypesParser;

/**
 * Main (singleton) class of this project.
 * 
 * Gets a project and manages and initiates all necessary steps for the program functionality.
 * 
 * All code is in the {@code execute(IProject)} method.
 * 
 * @author Nils Wilka
 * @version 1.0, 28.07.2017
 */
public final class JavaAnnotations2JML {

    /**
     * The singleton instance of this class.
     */
    private static Optional<JavaAnnotations2JML> singleton = Optional.empty();

    /**
     * No instantiation for singleton class.
     */
    private JavaAnnotations2JML() {
        ////
    }

    /**
     * Gets the instance of this singleton class.
     * 
     * @return The singleton class of Type {@code JavaAnnotations2JML}.
     */
    public static JavaAnnotations2JML getSingleton() {
        if (!singleton.isPresent()) {
            singleton = Optional.of(new JavaAnnotations2JML());
        }
        return singleton.get();
    }

    /**
     * Execution of the this program.
     * 
     * Copies the given project and converts it to a java project. Then parses its source java files
     * and including the java files for the confidentiality specification.
     * 
     * All relevant information of the java types regarding the confidentiality specification is
     * gathered in the third and fourth steps, which are then used to finally generate the JML
     * comments.
     * 
     * The JML confidentiality specification in the newly created project can then be used to verify
     * the confidentiality of the project.
     * 
     * @param project
     *            The project with java nature to be copied, parsed and annotated with JML comments.
     */
    public void execute(IProject project) {
        /* first step: copy and get java project */
        IJavaProject javaProject = null; // TODO
        try {
            javaProject = new ProjectParser(project).parse();
        } catch (ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        /* second step: parse java project for confidentiality package and java types */
        JavaProjectParser sourceRepoParser = new JavaProjectParser(javaProject);
        ConfidentialityRepositoryParser confRepoParser = new ConfidentialityRepositoryParser(javaProject);

        JavaAnnotations2JMLParser<?, ?>[] parsers = { sourceRepoParser, confRepoParser };

        for (JavaAnnotations2JMLParser<?, ?> parser : parsers) {
            try {
                parser.parse();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        List<TopLevelType> projectTopLevelTypes = sourceRepoParser.getResult();
        ConfidentialitySpecification specification = confRepoParser.getResult();

        /*
         * third step
         */
        TopLevelTypesParser topLevelTypesParser = new TopLevelTypesParser(projectTopLevelTypes);
        List<TopLevelType> allTopLevelTypes = null;
        try {
            allTopLevelTypes = topLevelTypesParser.parse();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /* fourth step: for each type get required and provided types */
        ServiceTypeParser serviceTypeParser = new ServiceTypeParser(projectTopLevelTypes);
        List<AbstractServiceType> serviceTypes = null; // TODO
        try {
            serviceTypes = serviceTypeParser.parse();
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // unique set
        Set<TopLevelType> serviceTypeTopLevelTypes = new HashSet<>();
        for (AbstractServiceType serviceType : serviceTypes) {
            serviceTypeTopLevelTypes.add(serviceType.getParentType());
        }

        for (TopLevelType topLevelType : serviceTypeTopLevelTypes) {
            MethodParser methodParser = new MethodParser(specification, topLevelType, topLevelType);
            try {
                methodParser.parse();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        ServiceCreator serviceCreator = new ServiceCreator(serviceTypes);
        serviceCreator.parse();

        /* sixth step: generate jml comments for each type and each data set */
        JMLCommentsGenerator generator = new JMLCommentsGenerator(projectTopLevelTypes);
        try {
            generator.transformAllAnnotationsToJml();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
