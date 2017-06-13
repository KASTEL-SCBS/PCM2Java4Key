package edu.kit.kastel.scbs.pcm2java4KeY.generator

import edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorDataTypes

final class PCM2Java4KeYGeneratorDataTypes extends PCM2JavaGeneratorDataTypes {
	
	
	private val boolean publicFields
	
	new(boolean publicFields) {
		this.publicFields = publicFields
	}
		
	override generateFieldVisibilityModifier() {
		if (publicFields) "public" 
		else "private"
	}
}