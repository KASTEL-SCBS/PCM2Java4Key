package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ConfidentialityRepositoryParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.JavaProjectParser;

/* parse java project for confidentiality package and java types */
public class ParseJavaProject implements Command {

    private IJavaProjectProvider provider;

    public ParseJavaProject(IJavaProjectProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute() {
        IJavaProject javaProject = provider.getIJavaProject();
        JavaProjectParser sourceRepoParser = new JavaProjectParser(javaProject);
        ConfidentialityRepositoryParser confRepoParser = new ConfidentialityRepositoryParser(javaProject);
        try {
            provider.setConfidentialitySpecification(confRepoParser.parse());
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            provider.setTopLevelTypes(sourceRepoParser.parse());
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
