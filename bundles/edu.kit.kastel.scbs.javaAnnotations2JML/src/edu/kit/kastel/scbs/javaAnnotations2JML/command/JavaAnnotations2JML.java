package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Main command of this program.
 * 
 * Gets a project and manages and initiates all necessary steps for the program functionality.
 * 
 * All code is found in the {@code execute} method.
 * 
 * @author Nils Wilka
 * @version 1.3, 14.09.2017
 */
public final class JavaAnnotations2JML extends Command {

    private List<Command> commands;

    private IProject project;

    private IJavaProject javaProject;

    private ConfidentialitySpecification specification;

    private List<TopLevelType> projectTopLevelTypes;

    private List<AbstractServiceType> serviceTypes;

    /**
     * Creates a command for the execution of this program with a given {@code IProject}.
     * 
     * @param project
     *            The project with java nature to be copied, parsed and annotated with JML comments.
     */
    public JavaAnnotations2JML(IProject project) {
        this.project = project;
    }

    /**
     * Execution of the this program.
     * 
     * Copies the given project and converts it to a java project. Then parses its source java files
     * and including the java files for the confidentiality specification.
     * 
     * To generate the JML comments, the {@code TopLevelType.Field}s and super types of each project
     * type are used to create {@code AbstractServiceType}s. Their methods including the information
     * flow annotations are parsed and finally the {@code Service}s created for them.
     * 
     * After all necessary preparation has been done, the JML comments can be generated.
     * 
     * The JML confidentiality specification in the newly created project can then be used to verify
     * the confidentiality of the project.
     */
    @Override
    public void execute() {
        setup();
        Command command = commands.get(0);
        for (Iterator<Command> iterator = commands.iterator(); iterator.hasNext() && !command.aborted();) {
            command = (Command) iterator.next();
            command.execute();
        }
    }

    /**
     * Sets up all commands to be executed in this program.
     */
    private void setup() {
        commands = new LinkedList<>();
        commands.add(new SetJavaProjectCommand(() -> {
            return project;
        }, x -> {
            javaProject = x;
        }));
        commands.add(new ConfidentialityRepositoryCreatorCommand(() -> {
            return javaProject;
        }, x -> {
            specification = x;
        }));
        commands.add(new TopLevelTypeCreatorCommand(() -> {
            return javaProject;
        }, x -> {
            projectTopLevelTypes = x;
        }));
        commands.add(new ParseTopLevelTypeCommand(this::getTopLevelTypes));
        commands.add(new SetServiceTypesCommand(this::getTopLevelTypes, x -> {
            serviceTypes = x;
        }));
        commands.add(new SetMethodsCommand(this::getAbstractServiceTypes, () -> {
            return specification;
        }));
        commands.add(new CreateServicesCommand(this::getAbstractServiceTypes));
        commands.add(new GenerateJmlCommentsCommand(this::getTopLevelTypes));
    }

    /**
     * Gets the top level types.
     * 
     * @return The top level types.
     */
    public List<TopLevelType> getTopLevelTypes() {
        return this.projectTopLevelTypes;
    }

    /**
     * Gets the abstract service types.
     * 
     * @return The abstract service types.
     */
    public List<AbstractServiceType> getAbstractServiceTypes() {
        return this.serviceTypes;
    }
}
