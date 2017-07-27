package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;

public class DataSet {

    private String id;

    private String name;

    public DataSet(final String name) {
        this.name = name;
    }

    public DataSet(final String name, final String id) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return "DataSets." + id;
    }

    public String getName() {
        return name;
    }

    public static DataSet create(EnumConstantDeclaration enumConstantDeclaration) throws JavaModelException {
        List<Expression> arguments = enumConstantDeclaration.arguments();
        // TODO empty arguments
        // assert second argument is name and a StringLiteral
        return new DataSet(arguments.get(1).toString(), enumConstantDeclaration.getName().toString());
    }

    @Override
    public String toString() {
        return "DataSets." + getId() + "(" + getName() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof DataSet) {
            DataSet other = (DataSet) obj;
            return this.name.equals(other.getName()) && this.id.equals(other.getId());
        } else {
            return false;
        }
    }
}
