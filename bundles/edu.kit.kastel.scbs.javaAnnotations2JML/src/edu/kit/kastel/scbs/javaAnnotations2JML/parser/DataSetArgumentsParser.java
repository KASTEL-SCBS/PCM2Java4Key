package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;

/**
 * Java model parser for data sets. The values are temporary stored in a {@code DataSetArguments}
 * helper object.
 * 
 * @author Nils Wilka
 * @version 1.0, 15.08.2017
 */
public class DataSetArgumentsParser extends EnumConstantDeclarationParser<List<DataSetArguments>> {

    /**
     * Creates a new parser to get the data set values from the given list of enum constant
     * declarations in a helper class.
     * 
     * @param source
     *            A list of enum constant declarations to get the data set values from.
     */
    public DataSetArgumentsParser(List<EnumConstantDeclaration> source) {
        super(source);
    }

    @Override
    protected List<DataSetArguments> parseSource() throws ParseException {
        List<DataSetArguments> dataSets = new LinkedList<>();
        for (EnumConstantDeclaration enumConstantDeclaration : getSource()) {
            dataSets.add(readDataSetArguments(enumConstantDeclaration));
        }
        return dataSets;
    }

    /**
     * Gets the data set values from the arguments of the given enum constant declaration.
     * 
     * @param enumConstantDeclaration
     *            The enum constant declaration representing a data set.
     * @return The data set values extracted from the enum constant declaration
     */
    // the java doc of EnumConstantDeclaration#arguments() specifies the type 'Expression'
    @SuppressWarnings("unchecked")
    public DataSetArguments readDataSetArguments(EnumConstantDeclaration enumConstantDeclaration) {
        List<Expression> arguments = enumConstantDeclaration.arguments();
        // TODO empty arguments
        // assert second argument is name and a StringLiteral
        String id = arguments.get(0).toString();
        String name = arguments.get(1).toString();
        String enumConstantName = enumConstantDeclaration.getName().toString();
        return new DataSetArguments(removeQuotes(id), removeQuotes(name), enumConstantName);
    }
}
