package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;

/**
 * Abstract class for all parsers in this project.
 * 
 * Extending sub classes specify with the first type parameter what kind of source they get, which
 * must be set by the constructor. It can be accessed via {@code getSource()}. The second type
 * parameter specifies what kind of result this parser delivers.
 * 
 * The {@code parse(S)} method returns the result, which can also be accessed via the
 * {@code getResult()} method.
 * 
 * @author Nils Wilka
 * @version 1.1, 12.08.2017
 *
 * @param <S>
 *            The kind of source this parser is working on or changing.
 * @param <R>
 *            The kind of result this parser delivers.
 */
public abstract class JavaAnnotations2JMLParser<S, R> {

    private S source;

    private R result;

    /**
     * Constructs a new parser. Sets the source to be parsed or changed
     * 
     * @param source
     *            The source to be parsed or changed.
     */
    public JavaAnnotations2JMLParser(S source) {
        this.source = source;
    }

    /**
     * Main method to parse or change the given source and returns its result according to this
     * parser.
     * 
     * @return The result of this parser.
     * 
     * @throws ParseException
     *             if there was a problem while parsing the source.
     * 
     * @see getResult()
     */
    public R parse() throws ParseException {
        this.result = parseSource();
        return this.result;
    }

    /**
     * Defines the main method to parse or change the given source and has to return its result.
     * 
     * Has to be implemented by extending sub classes.
     * 
     * @return The result of this parser.
     * @throws ParseException
     *             if there was a problem while parsing the source.
     */
    protected abstract R parseSource() throws ParseException;

    /**
     * Gets the source of this parser, which will be accessed, parsed or changed.
     * 
     * @return The source of this parser.
     */
    public S getSource() {
        return this.source;
    }

    /**
     * Gets the result of this parser, after the {@code parse(S)} method was called.
     * 
     * @return The result of this parser, the same as from {@code parse(S)}, or null if a
     *         {@code ParseException} occurred.
     * 
     * @see {@code parse(S)}
     */
    public R getResult() {
        return this.result;
    }
}
