package edu.kit.kastel.scbs.javaAnnotations2JML;

/**
 * Represents an enum constant consisting of a enum type declaration name and the constant name.
 * Used for parsing enum constants for example in the information flow annotation.
 * 
 * Example: "DataSets.NAME".
 * 
 * @author Nils Wilka
 * @version 1.0, 06.08.2017
 */
public class EnumConstant {

    private String constantType;

    private String constantName;

    /**
     * Creates a new enum constant with the given type and name.
     * 
     * @param type
     *            The enum constant type name
     * @param name
     *            The enum constant name
     */
    public EnumConstant(String type, String name) {
        this.constantType = type;
        this.constantName = name;
    }

    /**
     * Creates a new enum constant with the given full name.
     * 
     * @param fullName
     *            The enum constant type name and enum constant name separated by a dot.
     */
    public EnumConstant(String fullName) {
        String[] string = fullName.split("\\.");
        assert string != null && string.length == 2 : "given name \"" + fullName + "\" does not meet requirements.";
        this.constantType = string[0];
        this.constantName = string[1];
    }

    /**
     * Return the enum constant type name before the dot.
     * 
     * @return The enum constant type name.
     */
    public String getConstantType() {
        return constantType;
    }

    /**
     * Return the enum constant name after the dot.
     * 
     * @return The enum constant name.
     */
    public String getConstantName() {
        return constantName;
    }

    /**
     * Return the full name of this enum constant.
     * 
     * Example: "DataSets.NAME".
     * 
     * @return The full name of this enum constant.
     */
    public String getFullName() {
        return constantType + "." + constantName;
    }

    /**
     * Convenience method for testing if this enum constant has an equal full name to the given one.
     * 
     * @param otherFullName
     *            An enum constant full name to compare to this one.
     * @return Whether this enum constants full name is equivalent to the given one.
     */
    public boolean equalsFullName(String otherFullName) {
        return this.getFullName().equals(otherFullName);
    }
}
