package edu.kit.kastel.scbs.javaAnnotations2JML.generator;

import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.EnumConstantDeclaration;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;

/**
 * Abstract generator for lists of {@code EnumConstantDeclaration}s.
 * 
 * This includes string manipulation and checking of java identifiers.
 * 
 * @author Nils Wilka
 * @version 1.0, 17.08.2017
 *
 * @param <R>
 *            The kind of result this parser delivers.
 */
public abstract class EnumConstantDeclarationGenerator<R>
        extends JavaAnnotations2JMLGenerator<List<EnumConstantDeclaration>, R> {

    private static final Collator ENGLISH_COLLATOR = Collator.getInstance(Locale.ENGLISH);

    private static final String[] JAVA_KEYWORDS = { "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "false", "final",
            "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
            "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try",
            "void", "volatile", "while" };

    /**
     * Creates a new java model parser for enum constant constant declarations.
     * 
     * @param source
     *            The list of enum constant declarations to parse.
     */
    public EnumConstantDeclarationGenerator(List<EnumConstantDeclaration> source) {
        super(source);
    }

    @Override
    protected abstract R scanSource() throws ParseException;

    /**
     * Removes all quotes from a given string.
     * 
     * @param string
     *            The string to remove the quotes from.
     * @return The given string without quotes
     */
    protected String removeQuotes(String string) {
        return string.replace("\"", "");
    }

    /**
     * Removes the start and end of the given {@code string}, which is the string representation of
     * an array with the type {@code arrayTypeString}. Then splits the given string into a list of
     * strings at each symbol ",".
     * 
     * @param arrayTypeString
     *            The type of the array in the {@code string}.
     * @param arrayString
     *            The string representation of an array with comma separated strings as values.
     * @return A list of the comma separated string values in the given array string representation.
     * @throws ParseException
     *             if the given string does not match an array string representation or if the array
     *             is empty.
     */
    protected List<String> parseArrayArgument(String arrayTypeString, String arrayString) throws ParseException {
        // example: new String[] {"a", "b"} -> "a", "b"
        String substring = trimArrayArgument(arrayTypeString, arrayString);
        List<String> result = splitArgumentsString(substring);
        return result;
    }

    /**
     * Removes the start and end of the given {@code string}, which is the string representation of
     * an array with the type {@code arrayTypeString}.
     * 
     * @param arrayTypeString
     *            The type of the array in the {@code string}.
     * @param string
     *            The string representation of an array.
     * @return The remaining string after being trimmed at the start and end.
     * @throws ParseException
     *             if the given string does not match an array string representation.
     */
    protected String trimArrayArgument(String arrayTypeString, String string) throws ParseException {
        if (!Pattern.matches("new " + arrayTypeString + "\\[\\]\\{" + ".+" + "\\}", string)) {
            throw new ParseException("Unexpected input as array: '" + string + "'");
        }
        return string.substring(7 + arrayTypeString.length(), string.length() - 1);
    }

    /**
     * Splits the given string into a list of strings at each symbol ",".
     * 
     * @param string
     *            The string to split.
     * @return A list of strings split at each ",".
     * @throws ParseException
     *             if the given string is empty.
     */
    protected List<String> splitArgumentsString(String string) throws ParseException {
        if (string.isEmpty()) {
            throw new ParseException("Missing array value.");
        }
        return Arrays.asList(string.split(","));
    }

    /**
     * Checks if a given string is a legal java identifier. That is the case when it matches the
     * java identifier regular expression and it is not a java keyword.
     * 
     * @param string
     *            The string to check.
     * @return True if the given string is a legal java identifier, else false.
     */
    protected boolean isLegalJavaIdentifier(String string) {
        return !isJavaKeyword(string) && isLegal(string);
    }

    /**
     * Checks whether the given string matches the java identifier regular expression.
     * 
     * @param string
     *            The string to check.
     * @return True if the given string matches the java identifier regular expression, else false.
     */
    protected boolean isLegal(String string) {
        return Pattern.matches("^([a-zA-Z_$][a-zA-Z\\d_$]*)$", string);
    }

    /**
     * Checks if a given string is a java keyword.
     * 
     * @param string
     *            The string to check.
     * @return True if the given string is a java keyword, else false.
     */
    protected boolean isJavaKeyword(String string) {
        return (Arrays.binarySearch(JAVA_KEYWORDS, string, ENGLISH_COLLATOR) >= 0);
    }
}
