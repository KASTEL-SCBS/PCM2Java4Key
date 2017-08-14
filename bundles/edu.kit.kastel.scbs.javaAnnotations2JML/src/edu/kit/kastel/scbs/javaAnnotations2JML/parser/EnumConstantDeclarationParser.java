package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.EnumConstantDeclaration;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;

public abstract class EnumConstantDeclarationParser<R>
        extends JavaAnnotations2JMLParser<List<EnumConstantDeclaration>, R> {

    private static final Collator ENGLISH_COLLATOR = Collator.getInstance(Locale.ENGLISH);

    private static final String[] JAVA_KEYWORDS = { "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "false", "final",
            "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
            "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try",
            "void", "volatile", "while" };

    public EnumConstantDeclarationParser(List<EnumConstantDeclaration> source) {
        super(source);
    }

    @Override
    protected abstract R parseSource() throws ParseException;

    protected String removeQuotes(String string) {
        return string.replace("\"", "");
    }

    protected boolean isLegalJavaIdentifier(String string) {
        return !isJavaKeyword(string) && isLegal(string);
    }

    protected boolean isLegal(String string) {
        return Pattern.matches("^([a-zA-Z_$][a-zA-Z\\d_$]*)$", string);
    }

    protected boolean isJavaKeyword(String string) {
        return (Arrays.binarySearch(JAVA_KEYWORDS, string, ENGLISH_COLLATOR) >= 0);
    }
}
