package edu.kit.kastel.scbs.pcm2java4KeY.handler

class DefaultUserConfiguration implements edu.kit.kastel.scbs.pcm2java4KeY.handler.UserConfiguration {
	
	val boolean publicFields;
	val boolean replaceStringsWithCharArrays;

	new() {
		this(true, true)
	}
	
	new(boolean publicFields, boolean replaceStringsWithCharArrays) {
		this.publicFields = publicFields
		this.replaceStringsWithCharArrays = replaceStringsWithCharArrays
	}
	
	override publicFields() {
		return publicFields
	}
	
	override replaceStringsWithCharArrays() {
		return replaceStringsWithCharArrays
	}
}
