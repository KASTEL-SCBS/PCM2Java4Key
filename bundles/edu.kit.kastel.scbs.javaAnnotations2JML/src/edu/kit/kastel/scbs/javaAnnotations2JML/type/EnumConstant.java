package edu.kit.kastel.scbs.javaAnnotations2JML.type;

/**
 * Enum constant implementation. Used for parsing enum constants that do not have a type yet.
 * 
 * Example: parameters and data pairs in the information flow annotation.
 * 
 * @author Nils Wilka
 * @version 1.1, 14.08.2017
 */
public class EnumConstant implements EnumConstantInterface {

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

    @Override
    public String getEnumConstantType() {
        return constantType;
    }

    @Override
    public String getEnumConstantSimpleName() {
        return constantName;
    }

    @Override
    public String getEnumConstantFullName() {
        return constantType + "." + constantName;
    }
}
