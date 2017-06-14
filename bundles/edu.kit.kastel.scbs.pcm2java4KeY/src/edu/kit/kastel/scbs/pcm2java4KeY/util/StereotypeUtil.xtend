package edu.kit.kastel.scbs.pcm2java4KeY.util

import edu.kit.kastel.scbs.confidentiality.repository.ParametersAndDataPair
import java.util.ArrayList
import org.palladiosimulator.pcm.repository.OperationInterface
import org.palladiosimulator.pcm.repository.OperationSignature
import static extension edu.kit.ipd.sdq.commons.util.org.palladiosimulator.mdsdprofiles.api.StereotypeAPIUtil.*

class StereotypeUtil {
	
	/** Utility classes should not have a public or default constructor. */
	private new() {}
	
	static def Iterable<ParametersAndDataPair> getAllParamatersAndDataPairs(OperationInterface iface) {		
		val result = new ArrayList<ParametersAndDataPair>
		result.addAll(iface.parametersAndDataPairs)
		result.addAll(iface.allSignatureParametersAndDataPairs)
		return result
	}
	
	static def Iterable<ParametersAndDataPair> getParametersAndDataPairs(OperationInterface iface) {
		iface.getTaggedValues("InformationFlow", "parametersAndDataPairs", ParametersAndDataPair)
	}
	
	static def Iterable<ParametersAndDataPair> getAllParametersAndDataPairsAppliedToSignature(OperationInterface iface, OperationSignature operationSignature) {
		val result = new ArrayList<ParametersAndDataPair>
		result.addAll(iface.parametersAndDataPairs)
		result.addAll(operationSignature.parametersAndDataPairs)
		return result
	}
	
	static def Iterable<ParametersAndDataPair> getAllSignatureParametersAndDataPairs(OperationInterface iface) {
		iface.signatures__OperationInterface.map[it.parametersAndDataPairs].flatten
	}
	
	static def Iterable<ParametersAndDataPair> getParametersAndDataPairs(OperationSignature operationSignature) {
		operationSignature.getTaggedValues("InformationFlow", "parametersAndDataPairs", ParametersAndDataPair)
	}
	
}