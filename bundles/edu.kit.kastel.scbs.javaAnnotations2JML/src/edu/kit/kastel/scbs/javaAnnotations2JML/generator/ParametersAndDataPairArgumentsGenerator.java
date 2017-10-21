package edu.kit.kastel.scbs.javaAnnotations2JML.generator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;

/**
 * Generator for parameters and data pairs. The values are temporary stored in a
 * {@code ParametersAndDataPairArguments} helper object to be used in the context of a
 * {@code ConfidentialitySpecification} to create a {@code ParametersAndDataPair}.
 * 
 * @author Nils Wilka
 * @version 1.0, 17.08.2017
 */
public class ParametersAndDataPairArgumentsGenerator
        extends EnumConstantDeclarationGenerator<List<ParametersAndDataPairArguments>> {

    /**
     * Creates a new generator to get the parameters and data pair values from the given list of
     * enum constant declarations in a helper class.
     * 
     * @param source
     *            The list {@code EnumConstantDeclaration}s to scan.
     */
    public ParametersAndDataPairArgumentsGenerator(final List<EnumConstantDeclaration> source) {
        super(source);
    }

    @Override
    protected List<ParametersAndDataPairArguments> scanSource() throws ParseException {
        final List<ParametersAndDataPairArguments> parameterAndDataPairs = new LinkedList<>();
        for (final EnumConstantDeclaration enumConstantDeclaration : getSource()) {
            parameterAndDataPairs.add(readParametersAndDataPairArguments(enumConstantDeclaration));
        }
        return parameterAndDataPairs;
    }

    /**
     * Gets the data set values from the arguments of the given enum constant declaration.
     * 
     * @param enumConstantDeclaration
     *            The enum constant declaration representing a parameters and data pair.
     * @return The parameters and data pair values extracted from the enum constant declaration
     * @throws ParseException
     *             if the given {@code EnumConstantDeclaration} arguments are invalid, i.e. if the
     *             parameter sources are no represented by a string array or the data sets not by a
     *             DataSets array.
     */
    // the java doc of EnumConstantDeclaration#arguments() specifies the type 'Expression'
    @SuppressWarnings("unchecked")
    public ParametersAndDataPairArguments readParametersAndDataPairArguments(
            final EnumConstantDeclaration enumConstantDeclaration) throws ParseException {
        final List<Expression> arguments = enumConstantDeclaration.arguments();
        // TODO empty arguments
        // TODO check if name is valid identifier
        String enumConstantName = enumConstantDeclaration.getName().toString();
        final List<String> parameterSources = scanParameterSources(arguments.get(0).toString());
        final List<EnumConstant> dataSetEnumConstants = scanDataSetEnumConstants(arguments.get(1).toString());
        return new ParametersAndDataPairArguments(enumConstantName, dataSetEnumConstants, parameterSources);
    }

    /**
     * Gets the parameter source strings as a list from the string representation of a string array.
     * 
     * Example: Returned list contains elements a and b if the given string has the form:
     * 
     * "new String[] {"a", "b"}"
     * 
     * @param argument
     *            The string representation of an array string.
     * @return A list of string representations of parameter sources
     * @throws ParseException
     *             if the given string does not match an array string representation with the type
     *             {@code String} or if the array is empty.
     */
    private List<String> scanParameterSources(final String argument) throws ParseException {
        final List<String> parameterSourceStrings = parseArrayArgument("String", argument);
        final List<String> result = new ArrayList<String>(parameterSourceStrings.size());
        for (final String string : parameterSourceStrings) {
            String toAdd = removeQuotes(string);
            if (isSpecial(toAdd)) {
                toAdd = toAdd.substring(1);
            }
            result.add(toAdd);
        }
        return result;
    }

    /**
     * Checks if the given string starts with a backslash.
     * 
     * @param parameterSource
     *            The string to check.
     * @return True if the given string starts with a backslash, else false.
     */
    private boolean isSpecial(String parameterSource) {
        return parameterSource.charAt(0) == '\\';
    }

    /**
     * Extracts the enum constants representing data sets in a list from the string representation
     * of an eum constant array.
     * 
     * @param argument
     *            The string representation of an array "DataSets".
     * @return A list of enum constants representing data sets.
     * @throws ParseException
     *             if the given string does not match an array string representation with the type
     *             {@code DataSets} or if the array is empty.
     */
    private List<EnumConstant> scanDataSetEnumConstants(final String argument) throws ParseException {
        final List<String> dataSetStrings = parseArrayArgument("DataSets", argument);
        return dataSetStrings.stream().map(EnumConstant::new).collect(Collectors.toList());
    }
}
