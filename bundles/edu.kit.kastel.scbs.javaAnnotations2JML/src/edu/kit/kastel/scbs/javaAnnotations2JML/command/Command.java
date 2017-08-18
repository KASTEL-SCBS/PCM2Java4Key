package edu.kit.kastel.scbs.javaAnnotations2JML.command;

/**
 * Interface for commands. Commands are program steps to be executed.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public interface Command {

    /**
     * Executes the command.
     */
    public void execute();
}
