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

/**
 * This class is used to create KeY compatible Java source code from PCM models. 
 * The generated code contains annotations that represent confidentiality stereotypes in the PCM model which can be transformed into KeY-verifiable JML annotations.
 *
 * @author Moritz Behr
 * @version 0.1
 */
class PCM2Java4KeYGenerator extends PCM2JavaGenerator {
	
	private val UserConfiguration userConfig // current user configuration
	private val ArrayList<StereotypeApplication> stereotypeApplications // list of stereotype applications found in the PCM model that is currently being processed
	
	/**
	 * Creates new PCM2Java4KeYGenerator using the default user configuration
	 */
	new() {
		this(new DefaultUserConfiguration)
	}
	
	/**
	 * Creates new PCM2Java4KeYGenerator using the given user configuration
	 * 
	 * @param userConfiguration the user configuration to be used 
	 */
	new(UserConfiguration userConfiguration) {
		userConfig = userConfiguration
		generatorDataTypes = new PCM2Java4KeYGeneratorDataTypes(userConfig.publicFields)
		generatorClassifier = new PCM2Java4KeYGeneratorClassifier
		stereotypeApplications = new ArrayList<StereotypeApplication>
	}
	
    /**
     * Makes necessary changes to the generated code to make it suitable for KeY.
     * Replaces strings with char arrays.
     * 
     * @param contents code to be processed
     * @return the processed code
     */	
	override postProcessGeneratedContents(String contents) {
		var processed = contents
		if (userConfig.replaceStringsWithCharArrays) {
			// TODO support many dimensional String arrays
			processed = processed.replace("	String ", "	Character[] ").replace("	String[]" , " Character[][]").replace("  String[0]" , " Character[0][0]")
			processed = processed.replace(" String ", " Character[] ").replace(" String[]" , " Character[][]").replace(" String[0]" , " Character[0][0]")
			processed = processed.replace(",String ", ",Character[] ").replace(",String[]", ",Character[][]").replace(",String[0]", ",Character[0][0]")
			processed = processed.replace("(String ", "(Character[] ").replace("(String[]", "(Character[][]").replace("(String[0]", "(Character[0][0]")
			
			processed = processed.replace("\"\"", "new Character[0]")
		}
		return processed
	}
	
	/**
	 * Generates additional content that is needed for the generated confidentiality annotations and adds it to the given list. 
	 * 
	 * @param contentsForFolderAndFileNames the list to which the additional contents will be added
	 */
	override generateAndAddOptionalContents(List<Triplet<String,String,String>> contentsForFolderAndFileNames) {
		contentsForFolderAndFileNames.addAll(generateConfidentialityCodeWithFolderAndFileNames(stereotypeApplications))
		stereotypeApplications.clear
	} 
	
	/**
	 * Filters stereotypes from other unexpected EObjects.
	 * 
	 * @element an EObject
	 */
	override generateContentUnexpectedEObject(EObject element) {
		switch(element) {
			StereotypeApplication: stereotypeApplications.add(element) // Needed for confidentiality code generation
		}
		return super.generateContentUnexpectedEObject(element)  //		"Cannot generate content for generic EObject '" + object + "'!"
	}
	
}