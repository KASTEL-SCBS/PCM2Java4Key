package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.EnumConstant;

/**
 * Represents the values necessary to create an {@code InformationFlowAnnotation}. In this case the
 * annotation can only be created in the context of a confidentiality specification.
 * 
 * @author Nils Wilka
 * @version 1.0, 08.08.2017
 */
public class InformationFlowAnnotationArguments {

    private List<EnumConstant> parametersAndDataPairEnumConstants;

    /**
     * Creates a new arguments object for the values of a {@code InformationFlowAnnotation}.
     * 
     * @param parametersAndDataPairEnumConstants
     *            The enum constant list to be converted to a object {@code ParametersAndDataPair}s.
     */
    public InformationFlowAnnotationArguments(List<EnumConstant> parametersAndDataPairEnumConstants) {
        this.parametersAndDataPairEnumConstants = parametersAndDataPairEnumConstants;
    }

    /**
     * Gets the values of this arguments object.
     * 
     * @return The enum constant list to be converted to a object {@code ParametersAndDataPair}s.
     */
    public List<EnumConstant> getParametersAndDataPairEnumConstants() {
        return parametersAndDataPairEnumConstants;
    }
}
