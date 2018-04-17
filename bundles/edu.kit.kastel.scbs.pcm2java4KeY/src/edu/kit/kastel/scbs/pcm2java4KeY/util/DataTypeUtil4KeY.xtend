package edu.kit.kastel.scbs.pcm2java4KeY.util

import edu.kit.ipd.sdq.mdsd.pcm2java.util.DataTypeUtil
import org.palladiosimulator.pcm.repository.DataType

/**
 * A utility class providing KeY-compatible utility methods for PCM data types
 * 
 * @author Moritz Behr
 * @version 0.1
 */
class DataTypeUtil4KeY {
    
    /** Utility classes should not have a public or default constructor. */
    private new() {
    }
    
    /**
     * TODO adjust comment
     * Returns "Iterable<name of class of inner type>" for a given collection data type.
     * If the inner type is null "Iterable<Object>" is returned.
     * 
     * @param dataType a PCM collection data type
     * @returns class name as used in Java code for the given collection data type.
     */
    static def String getClassNameOfDataType(DataType dataType) {
        DataTypeUtil.getClassNameOfDataType(dataType).replace("Iterable<", "").replace(">", "[]")
    }  
    
    static def String primitiveToReferenceName(String type) {
        DataTypeUtil.primitiveToReferenceName(type)
    }
    
    static def String removeArrayBrackets(String type) {
        if (type.endsWith("[]"))  {
            type.substring(0, type.length-2)
        }
    }
    
    /**
     * Replaces occurrences of "[]" with "[0]" in a given string. Is used to make reference names of array types usable in constructor calls.
     * 
     * @string a string
     * @return the given string with occurrences of "[]" replaced by "[0]"
     */
    static def String addArraySizes(String str) {
        return str.replace("[]", "[0]")
    }
}