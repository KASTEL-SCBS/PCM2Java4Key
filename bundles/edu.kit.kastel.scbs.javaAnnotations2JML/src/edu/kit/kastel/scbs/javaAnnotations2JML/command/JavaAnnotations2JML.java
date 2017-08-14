package edu.kit.kastel.scbs.javaAnnotations2JML.command;

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
 * All code is in the {@code execute} method.
 * 
 * @author Nils Wilka
 * @version 1.1, 14.08.2017
 */
public final class JavaAnnotations2JML implements Command, IProjectProvider, IJavaProjectProvider, TopLevelTypeProvider,
        ConfidentialitySpecificationProvider, ServiceTypeProvider, ServiceTypeAcceptor {

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
        for (Command command : commands) {
            command.execute();
        }
    }

    /**
     * Sets up all commands to be executed in this program.
     */
    private void setup() {
        commands = new LinkedList<>();
        commands.add(new SetJavaProject(this));
        commands.add(new ParseJavaProject(this));
        commands.add(new ParseTopLevelType(this));
        commands.add(new SetServiceTypes(this, this));
        commands.add(new SetMethods(this, this));
        commands.add(new SetServices(this));
        commands.add(new GenerateJmlComments(this));
    }

    @Override
    public IProject getIProject() {
        return project;
    }

    @Override
    public IJavaProject getIJavaProject() {
        return javaProject;
    }

    @Override
    public void setIJavaProject(IJavaProject javaProject) {
        this.javaProject = javaProject;
    }

    @Override
    public void setConfidentialitySpecification(ConfidentialitySpecification specification) {
        this.specification = specification;
    }

    @Override
    public void setTopLevelTypes(List<TopLevelType> topLevelTypes) {
        this.projectTopLevelTypes = topLevelTypes;
    }

    @Override
    public List<TopLevelType> getTopLevelTypes() {
        return this.projectTopLevelTypes;
    }

    @Override
    public ConfidentialitySpecification getConfidentialitySpecification() {
        return this.specification;
    }

    @Override
    public List<AbstractServiceType> getServiceTypes() {
        return this.serviceTypes;
    }

    @Override
    public void setServiceTypes(List<AbstractServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }
}
