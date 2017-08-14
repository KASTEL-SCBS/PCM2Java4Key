package edu.kit.kastel.scbs.javaAnnotations2JML.type;

/**
 * Represents an enum constant consisting of a enum type declaration name and the constant name.
 * Used for parsing enum constants for example in the information flow annotation.
 * 
 * Example: "DataSets.NAME".
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public interface EnumConstantInterface {

    /**
     * Return the enum constant type name before the dot.
     * 
     * @return The enum constant type name.
     */
    public String getEnumConstantType();

    /**
     * Return the enum constant name after the dot.
     * 
     * @return The enum constant name.
     */
    public String getEnumConstantSimpleName();

    /**
     * Return the full name of this enum constant.
     * 
     * Example: "DataSets.NAME".
     * 
     * @return The full name of this enum constant.
     */
    public String getEnumConstantFullName();

    /**
     * Convenience method for testing if this enum constant has an equal full name to the given one.
     * 
     * @param otherFullName
     *            An enum constant full name to compare to this one.
     * @return Whether this enum constants full name is equivalent to the given one.
     */
    public default boolean equalsEnumConstantFullName(String otherFullName) {
        return this.getEnumConstantFullName().equals(otherFullName);
    }
}
