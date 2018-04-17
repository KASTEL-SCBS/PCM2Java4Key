package edu.kit.kastel.scbs.pcm2java4KeY.util

import edu.kit.kastel.scbs.confidentiality.repository.ParametersAndDataPair
import java.util.ArrayList
import org.modelversioning.emfprofileapplication.StereotypeApplication
import org.palladiosimulator.pcm.repository.OperationInterface
import org.palladiosimulator.pcm.repository.OperationSignature

import static extension edu.kit.ipd.sdq.commons.util.org.palladiosimulator.mdsdprofiles.api.StereotypeAPIUtil.*

/**
 * A utility class providing utility methods for stereotypes.
 */
class StereotypeUtil {
	
	/** Utility classes should not have a public or default constructor. */
	private new() {}
	
	/**
	 * Returns all parameters and data pairs of stereotypes that are applied to either the given interface or one of it's operation signatures.
	 * 
	 * @param iface a PCM operation interface
	 * @return an iterable object containing the parameters and data pairs
	 */
	static def Iterable<ParametersAndDataPair> getAllParamatersAndDataPairs(OperationInterface iface) {		
		val result = new ArrayList<ParametersAndDataPair>
		result.addAll(iface.parametersAndDataPairs)
		result.addAll(iface.allSignatureParametersAndDataPairs)
		return result
	}
	
	/**
	 * Returns all parameters and data pairs of stereotypes that are applied to the given interface.
	 * 
	 * @param iface a PCM operation interface
	 * @return an iterable object containing the parameters and data pairs
	 */
	static def Iterable<ParametersAndDataPair> getParametersAndDataPairs(OperationInterface iface) {
		iface.getTaggedValues("InformationFlow", "parametersAndDataPairs", ParametersAndDataPair)
	}
	
	/**
     * Returns all parameters and data pairs of stereotypes that are applied to the given operation signature or .
     * 
     * @param iface a PCM operation signature
     * @return an iterable object containing the parameters and data pairs
     */
	static def Iterable<ParametersAndDataPair> getAllParametersAndDataPairsAppliedToSignature(OperationInterface iface, OperationSignature operationSignature) {
		val result = new ArrayList<ParametersAndDataPair>
		result.addAll(iface.parametersAndDataPairs)
		result.addAll(operationSignature.parametersAndDataPairs)
		return result
	}
	
	/**
     * Returns all parameters and data pairs of stereotypes that are applied to a operation signature of the given operation interface.
     * 
     * @param iface a PCM operation interface
     * @return an iterable object containing the parameters and data pairs
     */
	static def Iterable<ParametersAndDataPair> getAllSignatureParametersAndDataPairs(OperationInterface iface) {
		iface.signatures__OperationInterface.map[it.parametersAndDataPairs].flatten
	}
	
	/**
     * Returns all parameters and data pairs of stereotypes that are applied to a operation signature of the given operation interface.
     * 
     * @param iface a PCM operation interface
     * @return an iterable object containing the parameters and data pairs
     */
	static def Iterable<ParametersAndDataPair> getParametersAndDataPairs(OperationSignature operationSignature) {
		operationSignature.getTaggedValues("InformationFlow", "parametersAndDataPairs", ParametersAndDataPair)
	}
	
	/*
	 * Only stereotype applications that are applied to either an operation interface or an operation signature are supported.
	 */
	static def Iterable<ParametersAndDataPair> getParametersAndDataPairs(Iterable<StereotypeApplication> applications) {
		applications.getTaggedValues("parametersAndDataPairs", ParametersAndDataPair)
	}
	
}