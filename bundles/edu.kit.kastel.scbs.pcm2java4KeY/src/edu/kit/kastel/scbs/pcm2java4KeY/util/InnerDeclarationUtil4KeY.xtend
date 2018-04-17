package edu.kit.kastel.scbs.pcm2java4KeY.util

import org.palladiosimulator.pcm.repository.InnerDeclaration
import static extension edu.kit.kastel.scbs.pcm2java4KeY.util.DataTypeUtil4KeY.*

/**
 * A utility class providing utility methods for InnerDeclarations adapted to the needs of KeY
 */
class InnerDeclarationUtil4KeY {
    
    /** Utility classes should not have a public or default constructor. */
    private new() {
    }   
    
    /**
     * Returns the name of the class of the data type, as used in Java and suitable for KeY, that is contained in the given InnerDeclaration.
     * 
     * @param declaration a PCM inner declaration
     * @return name of the class
     */ 
    static def String getInnerDeclarationClassName(InnerDeclaration declaration){
        declaration.datatype_InnerDeclaration?.classNameOfDataType
    }
    
}