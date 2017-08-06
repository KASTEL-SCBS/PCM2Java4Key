package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;

import edu.kit.kastel.scbs.javaAnnotations2JML.ParseException;

public class DataSetArgumentsParser extends EnumConstantDeclarationParser<List<DataSetArguments>> {

    public DataSetArgumentsParser(List<EnumConstantDeclaration> source) {
        super(source);
    }

    @Override
    public List<DataSetArguments> parse() throws ParseException {
        List<DataSetArguments> dataSets = new LinkedList<>();
        for (EnumConstantDeclaration enumConstantDeclaration : getSource()) {
            try {
                dataSets.add(readDataSetArguments(enumConstantDeclaration));
            } catch (JavaModelException jme) {
                Optional<String> message = Optional.ofNullable(jme.getMessage());
                throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
            }
        }
        return dataSets;
    }

    public DataSetArguments readDataSetArguments(EnumConstantDeclaration enumConstantDeclaration)
            throws JavaModelException {
        List<Expression> arguments = enumConstantDeclaration.arguments();
        // TODO empty arguments
        // assert second argument is name and a StringLiteral
        String id = arguments.get(0).toString();
        String name = arguments.get(1).toString();
        String enumConstantName = enumConstantDeclaration.getName().toString();
        return new DataSetArguments(removeQuotes(id), removeQuotes(name), enumConstantName);
    }
}
