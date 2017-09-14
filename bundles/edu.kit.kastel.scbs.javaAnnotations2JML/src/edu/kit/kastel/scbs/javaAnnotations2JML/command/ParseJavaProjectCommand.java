package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.ConfidentialityRepositoryParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.JavaProjectParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Command for parsing a java project, i.e. setting the confidentiality package and java types and
 * reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.1, 14.09.2017
 */
public class ParseJavaProjectCommand implements Command {

    private Supplier<IJavaProject> supplier;

    private Consumer<ConfidentialitySpecification> csConsumer;

    private Consumer<List<TopLevelType>> tltConsumer;

    /**
     * Creates a new java project parsing command with the given supplier and consumers.
     * 
     * @param supplier
     *            The supplier of a java project.
     * @param csConsumer
     *            To set the confidentiality package.
     * @param tltConsumer
     *            To set the java top level types.
     */
    public ParseJavaProjectCommand(Supplier<IJavaProject> supplier, Consumer<ConfidentialitySpecification> csConsumer,
            Consumer<List<TopLevelType>> tltConsumer) {
        this.supplier = supplier;
        this.csConsumer = csConsumer;
        this.tltConsumer = tltConsumer;
    }

    @Override
    public void execute() {
        IJavaProject javaProject = supplier.get();
        JavaProjectParser sourceRepoParser = new JavaProjectParser(javaProject);
        ConfidentialityRepositoryParser confRepoParser = new ConfidentialityRepositoryParser(javaProject);
        try {
            csConsumer.accept(confRepoParser.parse());
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            tltConsumer.accept(sourceRepoParser.parse());
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
