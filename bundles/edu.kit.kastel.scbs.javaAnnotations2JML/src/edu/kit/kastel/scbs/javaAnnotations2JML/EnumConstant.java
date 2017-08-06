package edu.kit.kastel.scbs.javaAnnotations2JML;

public class EnumConstant {

    private String constantType;

    private String constantName;

    public EnumConstant(String type, String name) {
        this.constantType = type;
        this.constantName = name;
    }

    public EnumConstant(String fullName) {
        String[] string = fullName.split("\\.");
        assert string != null && string.length == 2 : "given name \"" + fullName + "\" does not meet requirements.";
        this.constantType = string[0];
        this.constantName = string[1];
    }

    public String getConstantType() {
        return constantType;
    }

    public String getConstantName() {
        return constantName;
    }

    public String getFullName() {
        return constantType + "." + constantName;
    }

    public boolean equalsFullName(String otherFullName) {
        return this.getFullName().equals(otherFullName);
    }
}
