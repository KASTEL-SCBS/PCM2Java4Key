package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import edu.kit.kastel.scbs.javaAnnotations2JML.EnumConstant;

public class DataSetArguments {

    private String id;

    private String name;

    private EnumConstant enumConstant;

    public DataSetArguments(final String id, final String name, final String simpleName) {
        this.id = id;
        this.name = name;
        this.enumConstant = new EnumConstant("DataSets", simpleName);
    }

    public DataSetArguments(final String id, final String name, final EnumConstant enumConstant) {
        this.id = id;
        this.name = name;
        this.enumConstant = enumConstant;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EnumConstant getEnumConstant() {
        return enumConstant;
    }
}
