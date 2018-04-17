package edu.kit.kastel.scbs.pcm2java4KeY.handler

/**
 * Default user configurations for PCM2Java4KeYGenerator.
 * 
 * @author Moritz Behr
 * @version 0.1
 */
class DefaultUserConfiguration implements edu.kit.kastel.scbs.pcm2java4KeY.handler.UserConfiguration {
	
	val boolean publicFields // determines if generated fields are public or private
	val boolean replaceStringsWithCharArrays //determines if strings are replaced by char arrays
    
    /**
     * creates a new default user configuration
     */
	new() {
		this(true, true)
	}
	
	/**
	 * creates a new user configuration with the given parameters.
	 * 
	 * @param publicFields determines if generated fields are public or private
	 * @param replaceStringsWithCharArrays determines if strings are replaced by char arrays in the generated code
	 */
	new(boolean publicFields, boolean replaceStringsWithCharArrays) {
		this.publicFields = publicFields
		this.replaceStringsWithCharArrays = replaceStringsWithCharArrays
	}
	
	/**
	 * Returns if the generated fields should be public or private.
	 * 
	 * @return true, if the generated fields should be public, otherwise false
	 */
	override publicFields() {
		return publicFields
	}
	
	/**
     * Returns if strings should be replaced by char arrays in the generated code.
     * 
     * @return true, if strings should be replaced by char arrays, otherwise false
     */
	override replaceStringsWithCharArrays() {
		return replaceStringsWithCharArrays
	}
}
