package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;

/**
 * Helper class for the parsing of data sets. The values are temporary stored in this class, until
 * they are created in the context of a confidentiality specification.
 * 
 * @author Nils Wilka
 * @version 1.0, 15.08.2017
 */
public class DataSetArguments {

    private String id;

    private String name;

    private EnumConstant enumConstant;

    /**
     * Creates a new container for all data set values.
     * 
     * @param id
     *            The identification of this data set corresponding to the id in the pcm.
     * @param name
     *            The name of the data set.
     * @param simpleEnumConstantName
     *            The enum constant name without the type
     */
    public DataSetArguments(final String id, final String name, final String simpleEnumConstantName) {
        this.id = id;
        this.name = name;
        this.enumConstant = new EnumConstant("DataSets", simpleEnumConstantName);
    }

    /**
     * Creates a new container for all data set values.
     * 
     * @param id
     *            The identification of this data set corresponding to the id in the pcm.
     * @param name
     *            The name of the data set.
     * @param enumConstant
     *            The enum constant representing the data set.
     */
    public DataSetArguments(final String id, final String name, final EnumConstant enumConstant) {
        this.id = id;
        this.name = name;
        this.enumConstant = enumConstant;
    }

    /**
     * Gets the identification of the data set corresponding to the id in the pcm.
     * 
     * @return The identification of the data set corresponding to the id in the pcm.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the data set.
     * 
     * @return The name of the data set.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the enum constant representing the data set.
     * 
     * @return The enum constant representing the data set.
     */
    public EnumConstant getEnumConstant() {
        return enumConstant;
    }
}
