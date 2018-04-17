package edu.kit.kastel.scbs.pcm2java4KeY.generator

import edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorClassifier
import edu.kit.kastel.scbs.confidentiality.repository.ParametersAndDataPair
import org.palladiosimulator.pcm.repository.OperationSignature

import static extension edu.kit.kastel.scbs.pcm2java4KeY.util.StereotypeUtil.*
import static edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorConstants.*


/**
 * This class is used to generate KeY-compatible Java source code for basic components and operation interfaces from PCM models.
 *
 * @author Moritz Behr
 * @version 0.1
 */
final class PCM2Java4KeYGeneratorClassifier extends PCM2JavaGeneratorClassifier {
	
	/**
	 * Creates a new PCM2Java4KeYGeneratorClassifier
	 */
	new() {
		generatorHeadAndImports = new PCM2Java4KeYGeneratorHeadAndImports
	}			
	
	/**
	 * Generates imports and a head for the currently processed operation interface.
	 * If there are stereotypes applied to the interface, appropriate annotations are generated and added.
	 * 
	 * @return the generated imports and head
	 */				
	override String generateImportsAndInterfaceHead() '''«
		generatorHeadAndImports.generateImportsAndInterfaceHead(iface).replaceFirst("public interface", generateInterfaceAnnotations + "public interface")
	»'''
	
	/**
     * Generates a method declaration for the given operation signature.
     * If necessary, annotations representing applied stereotypes are also generated.
     * 
     * @param operationSignature a PCM operation signature
     * @return the generated method declaration
     */
	override generateMethodDeclaration(OperationSignature operationSignature) '''«
		generateAnnotations(operationSignature.parametersAndDataPairs)»«
		generateMethodDeclarationWithoutSemicolon(operationSignature)
	»'''
    
    /**
     * Generates Java annotations of InformationFlow type representing the given parameters and data pairs.
     * Generates one annotation per pair.
     * 
     * @param an iterable containing parameters and data pairs
     * @return the generated annotations as string
     */
	private def String generateAnnotations(Iterable<ParametersAndDataPair> parametersAndDataPairs) {
		if (parametersAndDataPairs != null && parametersAndDataPairs.size > 0) {
		return '''@InformationFlow(parametersAndDataPairs = «generateParametersAndDataPairArrayInitialization(parametersAndDataPairs)») // TODO: verify annotation«newLine»'''
		} else {
			return ""
		}
	}
	
	/**
	 * Generates Java annotations of InformationFlow type representing the stereotypes applied to the currently processed operation interface.
	 * Generates one annotation per stereotype.
	 * 
	 * @return the generated annotations as string
	 */
	private def String generateInterfaceAnnotations() '''«
			generateAnnotations(iface.parametersAndDataPairs)
	»'''
	
	/**
	 * Generates the initialization of an array containing all parameters and data pairs in the given iterable.
	 * For example, if the given iterable contaings pair1 and pair2, "{«pair1's name», «pair2's name»}" will be returned.
	 * 
	 * @param parameterAndDataPairs an iterable of parameters and data pairs
	 * @return the generated array initialization
	 */
	private def String generateParametersAndDataPairArrayInitialization(Iterable<ParametersAndDataPair> parametersAndDataPairs) '''«
		FOR pair : parametersAndDataPairs
		BEFORE '{'
		SEPARATOR ', '
		AFTER '}'
			»ParametersAndDataPairs.«pair.name»«
    	ENDFOR»'''
		
}		