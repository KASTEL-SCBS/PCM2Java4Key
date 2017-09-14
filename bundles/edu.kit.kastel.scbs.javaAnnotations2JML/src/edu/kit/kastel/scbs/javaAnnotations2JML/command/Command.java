package edu.kit.kastel.scbs.javaAnnotations2JML.command;

/**
 * Interface for commands. Commands are program steps to be executed.
 * 
 * Represents the command pattern, but with possibility to check if commands aborted their
 * execution.
 * 
 * @author Nils Wilka
 * @version 1.1, 14.09.2017
 */
public abstract class Command {

    private boolean aborted = false;

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * Aborts the execution of this command or stops with error.
     */
    protected void abort() {
        this.aborted = true;
    }

    /**
     * Checks if the execution of this command was aborted.
     * 
     * @return True if the execution of this command was aborted, e.g. because execution failed.
     */
    public boolean aborted() {
        return aborted;
    }
}
