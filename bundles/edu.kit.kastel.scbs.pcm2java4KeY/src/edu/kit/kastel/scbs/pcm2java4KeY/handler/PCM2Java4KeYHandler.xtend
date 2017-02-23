package edu.kit.kastel.scbs.pcm2java4KeY.handler

import edu.kit.ipd.sdq.mdsd.ecore2txt.handler.AbstractEcoreIFile2TxtHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import edu.kit.ipd.sdq.mdsd.ecore2txt.util.Ecore2TxtUtil
import edu.kit.kastel.scbs.pcm2java4KeY.generator.PCM2Java4KeYGeneratorModule
import edu.kit.kastel.scbs.pcm2java4KeY.generator.PCM2Java4KeYGenerator
import org.eclipse.core.resources.IFile
import java.util.List

class PCM2Java4KeYHandler extends AbstractEcoreIFile2TxtHandler {
	
	override getPlugInID() '''edu.kit.kastel.scbs.pcm2java4KeY'''
	
	override executeEcore2TxtGenerator(List<IFile> filteredSelection, ExecutionEvent event, String plugInID) throws ExecutionException {
		Ecore2TxtUtil.generateFromSelectedFilesInFolder(filteredSelection,new PCM2Java4KeYGeneratorModule(),new PCM2Java4KeYGenerator(), false, false)
	}
	
}