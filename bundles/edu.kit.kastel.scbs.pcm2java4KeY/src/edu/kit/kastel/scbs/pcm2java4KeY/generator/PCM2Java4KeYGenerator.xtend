package edu.kit.kastel.scbs.pcm2java4KeY.generator

import edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGenerator
import edu.kit.kastel.scbs.pcm2java4KeY.handler.DefaultUserConfiguration
import edu.kit.kastel.scbs.pcm2java4KeY.handler.UserConfiguration

class PCM2Java4KeYGenerator extends PCM2JavaGenerator {
	
	private val UserConfiguration userConfig
	
	new() {
		this(new DefaultUserConfiguration)
	}
	
	new(UserConfiguration userConfiguration) {
		userConfig = userConfiguration
		generatorDataTypes = new PCM2Java4KeYGeneratorDataTypes(userConfig.publicFields)
		generatorClassifier = new PCM2Java4KeYGeneratorClassifier
	}			
	
	override postProcessGeneratedContents(String contents) {
		var processed = contents
		if (userConfig.replaceStringsWithCharArrays) {
			// TODO support many dimensional String arrays
			processed = processed.replace(" String ", " char[] ").replace(" String[]" , "char[][]")
			processed = processed.replace(",String ", ",char[] ").replace(",String[]", ",char[][]")
			processed = processed.replace("(String ", "(char[] ").replace("(String[]", "(char[][]")
		}
		return processed
	}
	
}