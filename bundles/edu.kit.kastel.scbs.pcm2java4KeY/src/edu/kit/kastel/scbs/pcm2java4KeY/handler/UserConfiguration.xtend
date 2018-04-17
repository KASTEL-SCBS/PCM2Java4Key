package edu.kit.kastel.scbs.pcm2java4KeY.handler

/**
 * Interface for user configurations for PCM2Java4KeYGenerator.
 * 
 * @author Moritz Behr
 * @version 0.1
 */
interface UserConfiguration {
	def boolean publicFields() // determines if generated fields are public or private
	def boolean replaceStringsWithCharArrays() // determines if char arrays are used instead of strings
}
