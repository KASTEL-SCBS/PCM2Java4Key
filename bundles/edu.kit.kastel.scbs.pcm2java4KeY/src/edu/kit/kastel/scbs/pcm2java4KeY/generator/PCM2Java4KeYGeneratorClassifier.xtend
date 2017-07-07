package edu.kit.kastel.scbs.pcm2java4KeY.generator

import edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorClassifier
import edu.kit.kastel.scbs.confidentiality.repository.ParametersAndDataPair
import org.palladiosimulator.pcm.repository.OperationSignature

import static edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorConstants.*

import static extension edu.kit.kastel.scbs.pcm2java4KeY.util.StereotypeUtil.*

final class PCM2Java4KeYGeneratorClassifier extends PCM2JavaGeneratorClassifier {
	
	new() {
		generatorHeadAndImports = new PCM2Java4KeYGeneratorHeadAndImports
	}			
					
	override String generateImportsAndHead() '''«
		generatorHeadAndImports.generateImportsAndInterfaceHead(iface).replaceFirst("public interface", generateInterfaceAnnotations + "public interface")
	»'''
	
	override generateMethodDeclaration(OperationSignature operationSignature) '''«
		generateAnnotations(operationSignature.parametersAndDataPairs)»«
		generateMethodDeclarationWithoutSemicolon(operationSignature)
	»'''

	private def String generateAnnotations(Iterable<ParametersAndDataPair> parametersAndDataPairs) '''«
		»@InformationFlow(«generateParametersAndDataPairArray(parametersAndDataPairs)») // TODO: verify annotation"»
	'''
	
	private def String generateInterfaceAnnotations() '''«
			generateAnnotations(iface.parametersAndDataPairs)
	»'''
	
	private def String generateParametersAndDataPairArray(Iterable<ParametersAndDataPair> parametersAndDataPairs) '''«
		FOR pair : parametersAndDataPairs // "@InformationFlow(ParametersAndDataPairs." + parametersAndDataPair.name +") // TODO: verify annotation"
		BEFORE '{'
		SEPARATOR ', '
		AFTER '}'
			»ParametersAndDataPairs.«pair.name»«
    	ENDFOR»'''
		
}		