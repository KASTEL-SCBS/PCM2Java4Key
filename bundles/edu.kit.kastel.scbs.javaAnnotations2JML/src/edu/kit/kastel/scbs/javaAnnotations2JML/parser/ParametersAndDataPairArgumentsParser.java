package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;

public class ParametersAndDataPairArgumentsParser
        extends EnumConstantDeclarationParser<List<ParametersAndDataPairArguments>> {

    public ParametersAndDataPairArgumentsParser(List<EnumConstantDeclaration> source) {
        super(source);
    }

    @Override
    protected List<ParametersAndDataPairArguments> parseSource() throws ParseException {
        List<ParametersAndDataPairArguments> parameterAndDataPairs = new LinkedList<>();
        for (EnumConstantDeclaration enumConstantDeclaration : getSource()) {
            try {
                parameterAndDataPairs.add(readParametersAndDataPairArguments(enumConstantDeclaration));
            } catch (JavaModelException jme) {
                Optional<String> message = Optional.ofNullable(jme.getMessage());
                throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
            }
        }
        return parameterAndDataPairs;
    }

    // the java doc of EnumConstantDeclaration#arguments() specifies the type 'Expression'
    @SuppressWarnings("unchecked")
    public ParametersAndDataPairArguments readParametersAndDataPairArguments(
            EnumConstantDeclaration enumConstantDeclaration) throws JavaModelException, ParseException {
        List<Expression> arguments = enumConstantDeclaration.arguments();
        // TODO empty arguments
        // TODO check if name is valid identifier
        String enumConstantName = enumConstantDeclaration.getName().toString();
        List<String> parameterSources = parseParameterSources(arguments.get(0).toString());
        List<EnumConstant> dataSetEnumConstants = parseDataSetEnumConstants(arguments.get(1).toString());
        return new ParametersAndDataPairArguments(enumConstantName, dataSetEnumConstants, parameterSources);
    }

    private List<String> parseParameterSources(String argument) throws ParseException {
        List<String> parameterSourceStrings = parseArrayArgument("String", argument);
        return parameterSourceStrings.stream().map(e -> removeQuotes(e)).collect(Collectors.toList());
    }

    private List<EnumConstant> parseDataSetEnumConstants(String argument) throws ParseException {
        List<String> dataSetStrings = parseArrayArgument("DataSets", argument);
        return dataSetStrings.stream().map(EnumConstant::new).collect(Collectors.toList());
    }

    private List<String> parseArrayArgument(String arrayTypeString, String arrayString) throws ParseException {
        // example: new String[] {"a", "b"} -> "a", "b"
        String substring = trimArrayArgument(arrayTypeString, arrayString);
        List<String> result = splitArgumentsString(substring);
        return result;
    }

    private List<String> splitArgumentsString(String string) throws ParseException {
        String[] split = string.split(",");
        return Arrays.asList(split);
    }

    private String trimArrayArgument(String arrayTypeString, String string) throws ParseException {
        String arr;
        if (string.startsWith("new " + arrayTypeString + "[]{") && string.endsWith("}")) {
            arr = string.substring(7 + arrayTypeString.length(), string.length() - 1);
        } else {
            throw new ParseException("Unexpected input: " + string); // TODO
        }
        return arr;
    }
}
