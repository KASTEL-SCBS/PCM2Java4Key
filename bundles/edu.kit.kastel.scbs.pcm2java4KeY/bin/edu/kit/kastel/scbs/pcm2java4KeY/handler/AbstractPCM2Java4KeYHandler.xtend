package edu.kit.kastel.scbs.pcm2java4KeY.handler

import edu.kit.ipd.sdq.mdsd.ecore2txt.handler.AbstractEcoreIFile2TxtHandler
import edu.kit.ipd.sdq.mdsd.ecore2txt.util.Ecore2TxtUtil
import edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorModule
import edu.kit.kastel.scbs.pcm2java4KeY.generator.PCM2Java4KeYGenerator
import java.util.List
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.core.resources.IFile

abstract class AbstractPCM2Java4KeYHandler extends AbstractEcoreIFile2TxtHandler {
	
	override getPlugInID() '''edu.kit.ipd.sdq.mdsd.pcm2java'''
	
	override executeEcore2TxtGenerator(List<IFile> filteredSelection, ExecutionEvent event, String plugInID) throws ExecutionException {
		val publicFields = Boolean.parseBoolean(event.getParameter(plugInID + ".publicFieldsParameter"))
		val replaceStringsWithCharArrays = Boolean.parseBoolean(event.getParameter(plugInID + ".replaceStringsWithCharArraysParameter"))		
		val userConfiguration = new DefaultUserConfiguration(publicFields, replaceStringsWithCharArrays)
		Ecore2TxtUtil.generateFromSelectedFilesInFolder(filteredSelection,new PCM2JavaGeneratorModule(),new PCM2Java4KeYGenerator(userConfiguration), false, false)
	}
	
	abstract def void executeEcore2TxtGenerator(List<IFile> filteredSelection, UserConfiguration userConfiguration)
	
}
	