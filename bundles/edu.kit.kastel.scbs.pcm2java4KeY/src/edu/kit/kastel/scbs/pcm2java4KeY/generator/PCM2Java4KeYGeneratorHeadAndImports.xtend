package edu.kit.kastel.scbs.pcm2java4KeY.generator

import edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorHeadAndImports
import java.util.ArrayList
import java.util.Collection
import java.util.HashSet
import org.palladiosimulator.pcm.repository.OperationInterface

import static extension edu.kit.kastel.scbs.pcm2java4KeY.util.StereotypeUtil.*

/**
 *  This class is used to generate imports and a head for Java classes that represent PCM entities.
 *  The generated Java code is KeY compatible
 * 
 * @author Moritz Behr
 * @version 0.1
 */
class PCM2Java4KeYGeneratorHeadAndImports extends PCM2JavaGeneratorHeadAndImports {
	
	/**
     * Returns an iterable of all necessary elements to import in a Java interface representing the given PCM operation interface.
     * If there are 
     * 
     * @param iface a PCM operation interface
     * @return an iterable object containing types that are necessary to import
     * @throws UnsupportedGeneratorInput if it is not possible to generate the necessary imports for the given entity
     */
	override dispatch getElementsToImport(OperationInterface iface) {
		val elementsToImport = getTypesRequiredForAnnotations(iface)
		//TODO: single out "additional" imports in a single method -> better design
		elementsToImport.addAll(getTypesUsedInSignaturesOfProvidedServices(iface))
		return elementsToImport
	}
	
	/**
	 * Returns a set of all elements necessary to import when using confidentiality annotations (InformationFlow),
	 * if at least one information flow stereotype is aplied to the given operation interface or at least one of it's operation signatures.
	 * If there are no stereotypes applied, an empty set is returned.
	 * 
	 * @param iface a PCM operation interface
	 * @return set of elements that are necessary to import
	 */
	private def HashSet<Object> getTypesRequiredForAnnotations(OperationInterface iface) {
		val typesUsedInAnnotations = new HashSet<Object>
		if (iface.allParamatersAndDataPairs.length > 0) {
			typesUsedInAnnotations.add(PCM2Java4KeYGeneratorConfidentiality.parametersAndDataPairsAsFullyQualifiedType)
			typesUsedInAnnotations.add(PCM2Java4KeYGeneratorConfidentiality.informationFlowAsFullyQualifiedType)
		}
		return typesUsedInAnnotations
	}
	
	/**
     * Returns a list of types that are needed for using collection data types (e.g. these must be imported).
     * This is necessary as collection data types are not represented by classes in Java code but rather by some sort of collection of the respectable inner type.
     * 
     * @return a list of types that are necessary for using collection data types 
     */
	override def Collection<Object> getTypesRequiredForCollectionDataTypes() {
	    return new ArrayList<Object>
	}
}