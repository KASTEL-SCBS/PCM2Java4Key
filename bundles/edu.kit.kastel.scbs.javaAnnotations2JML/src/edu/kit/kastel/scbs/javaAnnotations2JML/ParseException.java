package edu.kit.kastel.scbs.javaAnnotations2JML;

public class ParseException extends Exception {

    private static final long serialVersionUID = 6965298902393491647L;

    private static final String DEFAULT_ERROR_MESSAGE = "Error while parsing.";

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable cause) {
        super(DEFAULT_ERROR_MESSAGE, cause);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
