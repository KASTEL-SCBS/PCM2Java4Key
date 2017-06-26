package edu.kit.kastel.scbs.pcm2java4KeY.generator

import edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorHeadAndImports
import org.palladiosimulator.pcm.repository.OperationInterface

import static extension edu.kit.kastel.scbs.pcm2java4KeY.util.StereotypeUtil.*
import java.util.HashSet

class PCM2Java4KeYGeneratorHeadAndImports extends PCM2JavaGeneratorHeadAndImports {
	
	override dispatch getElementsToImport(OperationInterface iface) {
		val elementsToImport = getTypesUsedInAnnotations(iface)
		elementsToImport.addAll((new PCM2JavaGeneratorHeadAndImports).getElementsToImport(iface))
		return elementsToImport
	}
	
	private def HashSet<Object> getTypesUsedInAnnotations(OperationInterface iface) {
		val typesUsedInAnnotations = new HashSet<Object>
		if (iface.allParamatersAndDataPairs.length > 0) {
			typesUsedInAnnotations.add(PCM2Java4KeYGeneratorConfidentiality.parametersAndDataPairsAsFullyQualifiedType)
			typesUsedInAnnotations.add(PCM2Java4KeYGeneratorConfidentiality.informationFlowAsFullyQualifiedType)
		}
		return typesUsedInAnnotations
	}
}