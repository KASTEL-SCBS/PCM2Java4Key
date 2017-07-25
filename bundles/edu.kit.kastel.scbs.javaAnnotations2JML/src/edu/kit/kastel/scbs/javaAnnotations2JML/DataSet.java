package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;

public class DataSet {

    private String name;

    private DataSet(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static DataSet create(EnumConstantDeclaration enumConstantDeclaration) throws JavaModelException {
        List<Expression> arguments = enumConstantDeclaration.arguments();
        // TODO empty arguments
        // assert second argument is name and a StringLiteral
        return new DataSet(arguments.get(1).toString());
    }
}
