package edu.kit.kastel.scbs.pcm2java4KeY.generator

import edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorDataTypes
import org.palladiosimulator.pcm.repository.CollectionDataType
import org.palladiosimulator.pcm.repository.InnerDeclaration

import static edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorConstants.*

import static extension edu.kit.kastel.scbs.pcm2java4KeY.util.DataTypeUtil4KeY.*
import static extension edu.kit.kastel.scbs.pcm2java4KeY.util.InnerDeclarationUtil4KeY.*

/**
 * This class is used to generate KeY-compatible Java source code for data types from PCM models.
 *
 * @author Moritz Behr
 * @version 0.1
 */
final class PCM2Java4KeYGeneratorDataTypes extends PCM2JavaGeneratorDataTypes {	
	
	private val boolean publicFields // determines if fields in generated classes are public.
	
	/**
	 * Creates a new PCM2Java4KeYGeneratorDataTypes.
	 * 
	 * @param publicFields determines if fields in generated classes will be public (true) or private (false) 
	 */
	new(boolean publicFields) {
		this.publicFields = publicFields
	}
	
	/**
	 * Generates the visibility modifier that should be used for generated fields.
	 * 
	 * @return visibility modifier to be used
	 */	
	override generateFieldVisibilityModifier() {
		if (publicFields) "public" 
		else "private"
	}
	
	override generateInnerDeclarationClassName(InnerDeclaration declaration) {
	    declaration.innerDeclarationClassName
	}
	
	/**
     * Generates a constructor call for a given collection data type.
     * As collection data types are represented by arrays in Java code for KeY, an array of the inner type of the given data type is created.
     * If the inner type of the given data type is null, an array of objects will be created instead.
     *  
     * @param dataType a PCM collection data type
     * @return the generated constructor call
     */
	override generateCollectionDataTypeConstructorCall(CollectionDataType type) {
	    val innerType = type.innerType_CollectionDataType
        if (innerType !== null) {
           return '''new «type.innerType_CollectionDataType.getClassNameOfDataType.primitiveToReferenceName.addArraySizes»[0];«newLine»'''
        } else {
           return '''null;«newLine»'''
        }
	}
    
    /**
     * Creates and returns a new head and imports generator that is suited for generating java code for KeY.
     * 
     * @return the created generator
     */
    override constructGeneratorHeadAndImports(){
        new PCM2Java4KeYGeneratorHeadAndImports
    }
        
}