package edu.kit.kastel.scbs.pcm2java4KeY.handler

import edu.kit.ipd.sdq.mdsd.ecore2txt.util.Ecore2TxtUtil
import edu.kit.kastel.scbs.pcm2java4KeY.generator.PCM2Java4KeYGenerator
import edu.kit.kastel.scbs.pcm2java4KeY.generator.PCM2Java4KeYGeneratorModule
import java.util.List
import org.eclipse.core.resources.IFile

/**
 * Handler class for the PCM2Java4KeY plug-in. 
 * 
 * @author Moritz Behr
 * @version 0.1
 */
class PCM2Java4KeYHandler extends AbstractPCM2Java4KeYHandler {
	
	/**
     * Returns the ID of this plug-in.
     * 
     * @return ID of this plug-in
     */
	override getPlugInID() '''edu.kit.kastel.scbs.pcm2java4KeY'''
	
	override executeEcore2TxtGenerator(List<IFile> filteredSelection, UserConfiguration userConfiguration) {
		Ecore2TxtUtil.generateFromSelectedFilesInFolder(filteredSelection,new PCM2Java4KeYGeneratorModule(),new PCM2Java4KeYGenerator(userConfiguration), false, false)
	}
	
}