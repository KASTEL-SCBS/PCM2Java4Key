package edu.kit.kastel.scbs.javaAnnotations2JML.generator;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;

/**
 * Abstract class for all generators in this project.
 * 
 * Extending sub classes specify with the first type parameter what kind of source they get, which
 * must be set by the constructor. It can be accessed via {@code getSource()}. The second type
 * parameter specifies what kind of result this generator delivers.
 * 
 * The {@code generate(S)} method returns the result, which can also be accessed via the
 * {@code getResult()} method.
 * 
 * @author Nils Wilka
 * @version 1.2, 16.09.2017
 *
 * @param <S>
 *            The kind of source this class is working on or changing.
 * @param <R>
 *            The kind of result this class delivers.
 */
public abstract class JavaAnnotations2JMLGenerator<S, R> {

    private S source;

    private R result;

    /**
     * Constructs a new generator. Sets the source to be scanned.
     * 
     * @param source
     *            The source to be scanned.
     */
    public JavaAnnotations2JMLGenerator(S source) {
        this.source = source;
    }

    /**
     * Main method to scan the given source and generate its result.
     * 
     * @return The result of this class.
     * 
     * @throws ParseException
     *             if there was a problem while scanning the source.
     * 
     * @see getResult()
     */
    public R generate() throws ParseException {
        this.result = scanSource();
        return this.result;
    }

    /**
     * Defines the main method to scan the given source and generate its result..
     * 
     * Has to be implemented by extending sub classes.
     * 
     * @return The result of this class.
     * @throws ParseException
     *             if there was a problem while scanning the source.
     */
    protected abstract R scanSource() throws ParseException;

    /**
     * Gets the source of this parser, which will be accessed or scanned.
     * 
     * @return The source for this class.
     */
    public S getSource() {
        return this.source;
    }

    /**
     * Gets the result of this class, after the {@code generate(S)} method was called.
     * 
     * @return The result of this class, the same as from {@code generate(S)}, or null if a
     *         {@code ParseException} occurred.
     * 
     * @see {@code parse(S)}
     */
    public R getResult() {
        return this.result;
    }
}
