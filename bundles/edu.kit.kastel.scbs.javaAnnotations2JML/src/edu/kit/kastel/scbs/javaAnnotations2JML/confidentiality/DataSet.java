package edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstantInterface;

/**
 * Represents a data set of the confidentiality specification. The id and the name correspond to the
 * values in the palladio component model. The enum constant name is the name of the field in the
 * 'DataSets' type.
 * 
 * @author Nils Wilka
 * @version 1.0, 03.08.2017
 */
public class DataSet implements EnumConstantInterface {

    private String id;

    private EnumConstant enumConstant;

    private String name;

    /**
     * Creates a new {@code DataSet} with the given id, name and enum constant name (without the
     * type).
     * 
     * @param id
     *            The identification of this data set corresponding to the id in the pcm.
     * @param name
     *            The name of the data set.
     * @param simpleEnumConstantName
     *            The enum constant name without the type
     */
    public DataSet(final String id, final String name, final String simpleEnumConstantName) {
        this(id, name, new EnumConstant("DataSets", simpleEnumConstantName));
    }

    /**
     * Creates a new {@code DataSet} with the given id, name and enum constant.
     * 
     * @param id
     *            The identification of this data set corresponding to the id in the pcm.
     * @param name
     *            The name of the data set.
     * @param enumConstant
     *            The enum constant representing the data set.
     */
    public DataSet(final String id, final String name, final EnumConstant enumConstant) {
        this.id = id;
        this.name = name;
        this.enumConstant = enumConstant;
    }

    /**
     * Gets the identification of this data set corresponding to the id in the pcm.
     * 
     * @return The identification of this data set corresponding to the id in the pcm.
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

    @Override
    public String getEnumConstantType() {
        return enumConstant.getEnumConstantType();
    }

    @Override
    public String getEnumConstantSimpleName() {
        return enumConstant.getEnumConstantSimpleName();
    }

    @Override
    public String getEnumConstantFullName() {
        return enumConstant.getEnumConstantFullName();
    }

    @Override
    public String toString() {
        return getEnumConstantFullName() + "(\"" + getName() + "\")";
    }

    /**
     * String representation of a list with data sets.
     * 
     * @param dataSets
     *            The list of data sets to get the string representation for.
     * @return A string representation of a list with data sets.
     */
    public static String toString(List<DataSet> dataSets) {
        StringBuilder sb = new StringBuilder(dataSets.get(0).toString());
        for (int i = 1; i < dataSets.size(); i++) {
            sb.append(", ").append(dataSets.get(i));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof DataSet) {
            DataSet other = (DataSet) obj;
            return this.id.equals(other.id);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
