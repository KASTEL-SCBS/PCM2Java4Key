package edu.kit.kastel.scbs.javaAnnotations2JML.exception;

/**
 * Caused by an unexpected or missing value while parsing.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public class ParseException extends Exception {

    private static final long serialVersionUID = 6965298902393491647L;

    private static final String DEFAULT_ERROR_MESSAGE = "Error while parsing.";

    /**
     * Constructs a new parse exception with the specified message.
     *
     * @param message
     *            The exception cause description.
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * Constructs a new parse exception with the specified cause.
     *
     * @param cause
     *            The exception cause.
     */
    public ParseException(Throwable cause) {
        super(DEFAULT_ERROR_MESSAGE, cause);
    }

    /**
     * Constructs a new parse exception with the specified cause message and cause.
     * 
     * @param message
     *            The exception cause description.
     * @param cause
     *            The exception cause.
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
