package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ProjectParser;

/* copy and get java project */
public class SetJavaProject implements Command {

    private IProjectProvider provider;

    public SetJavaProject(IProjectProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute() {
        try {
            provider.setIJavaProject(new ProjectParser(provider.getIProject()).parse());
        } catch (ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
    }

}
