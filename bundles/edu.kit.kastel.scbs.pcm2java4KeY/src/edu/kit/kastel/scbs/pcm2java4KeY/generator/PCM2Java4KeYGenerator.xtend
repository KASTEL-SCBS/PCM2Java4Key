package edu.kit.kastel.scbs.pcm2java4KeY.generator

import edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGenerator
import edu.kit.kastel.scbs.pcm2java4KeY.handler.DefaultUserConfiguration
import edu.kit.kastel.scbs.pcm2java4KeY.handler.UserConfiguration
import java.util.ArrayList
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.internal.xtend.util.Triplet
import org.modelversioning.emfprofileapplication.StereotypeApplication

import static edu.kit.kastel.scbs.pcm2java4KeY.generator.PCM2Java4KeYGeneratorConfidentiality.*

class PCM2Java4KeYGenerator extends PCM2JavaGenerator {
	
	private val UserConfiguration userConfig
	private val ArrayList<StereotypeApplication> stereotypeApplications
	
	new() {
		this(new DefaultUserConfiguration)
	}
	
	new(UserConfiguration userConfiguration) {
		userConfig = userConfiguration
		generatorDataTypes = new PCM2Java4KeYGeneratorDataTypes(userConfig.publicFields)
		generatorClassifier = new PCM2Java4KeYGeneratorClassifier
		stereotypeApplications = new ArrayList<StereotypeApplication>
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
	
	override generateAndAddOptionalContents(List<Triplet<String,String,String>> contentsForFolderAndFileNames) {
		contentsForFolderAndFileNames.addAll(generateConfidentialityCodeWithFolderAndFileNames(stereotypeApplications))
	} 
	
	override generateContentUnexpectedEObject(EObject element) {
		switch(element) {
			StereotypeApplication: stereotypeApplications.add(element) // Needed for confidentiality code generation
		}
		return super.generateContentUnexpectedEObject(element)  //		"Cannot generate content for generic EObject '" + object + "'!"
	}
	
}