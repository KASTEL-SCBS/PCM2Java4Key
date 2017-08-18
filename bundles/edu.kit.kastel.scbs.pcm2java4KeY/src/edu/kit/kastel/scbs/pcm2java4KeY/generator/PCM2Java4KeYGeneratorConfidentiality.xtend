package edu.kit.kastel.scbs.pcm2java4KeY.generator

import edu.kit.kastel.scbs.confidentiality.data.DataSet
import edu.kit.kastel.scbs.confidentiality.data.DataSetMap
import edu.kit.kastel.scbs.confidentiality.data.DataSetMapEntry
import edu.kit.kastel.scbs.confidentiality.repository.ParametersAndDataPair
import java.io.File
import org.eclipse.internal.xtend.util.Triplet
import org.modelversioning.emfprofileapplication.StereotypeApplication

import static edu.kit.ipd.sdq.mdsd.pcm2java.generator.PCM2JavaGeneratorConstants.*
import static extension edu.kit.kastel.scbs.pcm2java4KeY.util.StereotypeUtil.*

final class PCM2Java4KeYGeneratorConfidentiality {
	
	private static final String PACKAGE = "confidentialityRepository"
	
	private static final String DATA_SETS_CLASS_NAME = "DataSets"
	
	private static final String DATA_SETS = '''package «PACKAGE»;
public enum «DATA_SETS_CLASS_NAME» {
EXAMPLE // TODO: verify data sets.
	
	public final String id;
	public final String name;
	
	DataSets(String id, String name) {
		this.id = id;
	    this.name = name;
	}
}
'''
	
	private static final String DATA_SETS_EXAMPLE = '''    EXAMPLE("DS1","data set 1");'''
	
	private static final String DATA_SET_MAPS_CLASS_NAME = "DataSetMaps"

	private static final String DATA_SET_MAPS = '''package «PACKAGE»;
public enum «DATA_SET_MAPS_CLASS_NAME» {
EXAMPLE // TODO: verify data set maps
	
	private final String id;
	private final String name;
	
	DataSetMaps(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
'''

	private static final String DATA_SET_MAPS_EXAMPLE = '''    EXAMPLE("DSM1", "data set map 1");'''

	private static final String DATA_SET_MAP_ENRTIES_CLASS_NAME = "DataSetMapEntries"

	private static final String DATA_SET_MAP_ENTRIES = '''package «PACKAGE»;
public enum «DATA_SET_MAP_ENRTIES_CLASS_NAME» {
EXAMPLE // TODO: verify data set map entries
	
	public final String id;
	public final DataSetMaps dataSetMap;
	public final String name;
	
	private DataSetMapEntries(String id, DataSetMaps dataSetMap, String name) {
		this.id = id;
		this.dataSetMap = dataSetMap;
		this.name = name;
	}
}
'''

	private static final String DATA_SET_MAP_ENTRIES_EXAMPLE = '''    EXAMPLE_A("DSM1_E1", DataSetMaps.EXAMPLE, "EXAMPLE[A]");'''

	private static final String PARAMETERS_AND_DATA_PAIRS_CLASS_NAME = "ParametersAndDataPairs"

	private static final String PARAMETERS_AND_DATA_PAIRS = '''package «PACKAGE»;
public enum «PARAMETERS_AND_DATA_PAIRS_CLASS_NAME» {
EXAMPLE // TODO: verify parameters and data pairs
	
	public final String[] parameterSources;
	public final DataSets[] dataSets;
	public final DataSetMapEntries[] dataSetMapEntries;
	
	ParametersAndDataPairs(String[] parameterSources, DataSets[] dataSets, DataSetMapEntries[] dataSetMapEntries) {
		this.parameterSources = parameterSources;
		this.dataSets = dataSets;
		this.dataSetMapEntries = dataSetMapEntries;
	}	
}
'''
	
	private static final String PARAMETERS_AND_DATA_PAIRS_EXAMPLE = '''    EXAMPLE(new String[] {param1}, new DataSets[] {DataSets.EXAMPLE}, null);'''

	private static final String INFORMATION_FLOW_CLASS_NAME = "InformationFlow"

	private static final String INFORMATION_FLOW = '''package «PACKAGE»;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface «INFORMATION_FLOW_CLASS_NAME» {
	ParametersAndDataPairs[] parametersAndDataPairs();
}
'''
	
	private static final String FOLDER_NAME = "src-gen" + File.separator + "confidentialityRepository"
	
	private static final String FILE_EXT = ".java"

	static def Triplet<String, String, String>[] generateConfidentialityCodeWithFolderAndFileNames(Iterable<StereotypeApplication> stereotypeApplications) {
		val Triplet<String, String, String>[] result = newArrayOfSize(5)
		val userParametersAndDataPairs = getUserParametersAndDataPairs(stereotypeApplications)
		val userDataSets = getUserDataSets(userParametersAndDataPairs)
		val userDataSetMapEntries = getUserDataSetMapEntries(userParametersAndDataPairs)
		val userDataSetMaps = getUserDataSetMaps(userDataSetMapEntries)
		
		result.set(0, new Triplet(addUserDataSets(DATA_SETS, userDataSets), FOLDER_NAME, DATA_SETS_CLASS_NAME + FILE_EXT))
		result.set(1, new Triplet(addUserDataSetMaps(DATA_SET_MAPS, userDataSetMaps), FOLDER_NAME, DATA_SET_MAPS_CLASS_NAME + FILE_EXT))
		result.set(2, new Triplet(addUserDataSetMapEntries(DATA_SET_MAP_ENTRIES, userDataSetMapEntries), FOLDER_NAME, DATA_SET_MAP_ENRTIES_CLASS_NAME + FILE_EXT))
		result.set(3, new Triplet(addUserParametersAndDataPairs(PARAMETERS_AND_DATA_PAIRS, userParametersAndDataPairs), FOLDER_NAME,PARAMETERS_AND_DATA_PAIRS_CLASS_NAME + FILE_EXT))
		result.set(4, new Triplet(INFORMATION_FLOW, FOLDER_NAME, INFORMATION_FLOW_CLASS_NAME + FILE_EXT))
		return result
	}
	
	static def String getInformationFlowAsFullyQualifiedType() {
		return PACKAGE + "." + INFORMATION_FLOW_CLASS_NAME
	}
	
	static def String getDataSetsAsFullyQualifiedType() {
		return PACKAGE + "." + DATA_SETS_CLASS_NAME
	}
	
	static def String getDataSetMapsAsFullyQualifiedType() {
		return PACKAGE + "." + DATA_SET_MAPS_CLASS_NAME
	}
	
	static def String getDataSetMapEntriesAsFullyQualifiedType() {
		return PACKAGE +  "."  + DATA_SET_MAP_ENRTIES_CLASS_NAME
	}
	
	static def String getParametersAndDataPairsAsFullyQualifiedType() {
		return PACKAGE +  "." + PARAMETERS_AND_DATA_PAIRS_CLASS_NAME
	}
	
	private static def String addUserDataSets(String dataSets, Iterable<DataSet> userDataSets) {
		if (userDataSets.size > 0) {
			dataSets.replaceFirst("EXAMPLE", userDataSets.generateUserDataSetsString)
		} else {
			return dataSets.replaceFirst("EXAMPLE", DATA_SETS_EXAMPLE)
		}
	}
	
	private static def String addUserDataSetMaps(String dataSetMaps, Iterable<DataSetMap> userDataSetMaps) {
		if (userDataSetMaps.size > 0) {
			dataSetMaps.replaceFirst("EXAMPLE", userDataSetMaps.generateUserDataSetMapsString)
		} else {
			return dataSetMaps.replaceFirst("EXAMPLE", DATA_SET_MAPS_EXAMPLE)
		}
	}
	
	private static def String addUserDataSetMapEntries(String dataSetMapEntries, Iterable<DataSetMapEntry> userDataSetMapEntries) {
		if (userDataSetMapEntries.size > 0) {
			dataSetMapEntries.replaceFirst("EXAMPLE", userDataSetMapEntries.generateUserDataSetMapEntriesString)
		} else {
			return dataSetMapEntries.replaceFirst("EXAMPLE", DATA_SET_MAP_ENTRIES_EXAMPLE)
		}
	}
	
	private static def String addUserParametersAndDataPairs(String parametersAndDataPairs, Iterable<ParametersAndDataPair> userParametersAndDataPairs) {
		if (userParametersAndDataPairs.size > 0) {
			parametersAndDataPairs.replaceFirst("EXAMPLE", userParametersAndDataPairs.generateUserParametersAndDataPairsString)
		} else {
			return parametersAndDataPairs.replaceFirst("EXAMPLE", PARAMETERS_AND_DATA_PAIRS_EXAMPLE)
		}
	}
	
	private static def String generateUserDataSetsString(Iterable<DataSet> userDataSets)'''«
		FOR dataSet : userDataSets
		BEFORE "	"
		SEPARATOR "," + newLine +  "	"    
		AFTER ";"
		»«dataSet.name.ConvertToEnumValueName»("«dataSet.id»", "«dataSet.name»")«
		ENDFOR
	»''' 
	
	private static def String generateUserDataSetMapsString(Iterable<DataSetMap> userDataSetMaps)'''«
		FOR dataSetMap : userDataSetMaps
		BEFORE "	"
		SEPARATOR "," + newLine +  "	"    
		AFTER ";"
		»«dataSetMap.name.ConvertToEnumValueName»("«dataSetMap.id»", "«dataSetMap.name»")»«
		ENDFOR»
	''' 
	
	private static def String generateUserDataSetMapEntriesString(Iterable<DataSetMapEntry> userDataSetMapEntries)'''«
		FOR dataSetMapEntry : userDataSetMapEntries
		BEFORE "	"
		SEPARATOR "," + newLine +  "	"    
		AFTER ";"
		»«dataSetMapEntry.name.ConvertToEnumValueName»("«dataSetMapEntry.id»", DataSetMaps.«dataSetMapEntry.map.name.ConvertToEnumValueName», "«dataSetMapEntry.name»")»«
		ENDFOR»
	''' 
	
	private static def String generateUserParametersAndDataPairsString(Iterable<ParametersAndDataPair> userParametersAndDataPairs)'''«
		FOR parametersAndDataPair : userParametersAndDataPairs
		BEFORE "	"
		SEPARATOR "," + newLine +  "	"    
		AFTER ";"
		»«parametersAndDataPair.name»(«parametersAndDataPair.parameterSources.generateStringArrayConstructor», «parametersAndDataPair.dataTargets.filter(DataSet).generateDataSetArrayConstructor», «parametersAndDataPair.dataTargets.filter(DataSetMapEntry).generateDataSetMapEntryArrayConstructor»)«
	ENDFOR»''' 
	
	private static def String generateDataSetArrayConstructor(Iterable<DataSet> dataSets) {
		if (dataSets.size > 0) {
			return '''«
				FOR dataSet : dataSets
				BEFORE 'new DataSets[] {'
				SEPARATOR ', '
				AFTER '}'
					»DataSets.«dataSet.name.ConvertToEnumValueName»«
				ENDFOR
			»'''
		} 
		return "null"
	}
	
	private static def String generateDataSetMapEntryArrayConstructor(Iterable<DataSetMapEntry> dataSetMapEntries) {
		if (dataSetMapEntries.size > 0) {
			return '''«
				FOR dataSetMapEntry : dataSetMapEntries
				BEFORE 'new DataSetMapEntries[] {'
				SEPARATOR ', '
				AFTER '}'
					»DataSetMapEntries.«dataSetMapEntry.name.ConvertToEnumValueName»«
				ENDFOR
			»'''
		} 
		return "null"
	}
	
	private static def ParametersAndDataPair[] getUserParametersAndDataPairs(Iterable<StereotypeApplication> applications) {
		applications.parametersAndDataPairs.toSet
	}
	
	private static def DataSet[] getUserDataSets(Iterable<ParametersAndDataPair> parametersAndDataPairs) {
		parametersAndDataPairs.map[it.dataTargets].flatten.filter(DataSet).toSet
	}
	
	private static def DataSetMapEntry[] getUserDataSetMapEntries(Iterable<ParametersAndDataPair> parametersAndDataPairs) {
		parametersAndDataPairs.map[it.dataTargets].flatten.filter(DataSetMapEntry).toSet
	} 
	
	private static def DataSetMap[] getUserDataSetMaps(Iterable<DataSetMapEntry> dataSetMapEntries) {
		dataSetMapEntries.map[it.getMap].toSet
	}
	
	private static def String ConvertToEnumValueName(String name){
		var result = name.replace(" ", "_")
		for (var i = result.indexOf("[a-z][A-Z]"); i >= 0; i = result.indexOf("[a-z][A-Z]")) {
			result = result.substring(0, i).toUpperCase + "_" result.substring(i + 1)
		}
		return result.toUpperCase
	}
	
	private static def String generateStringArrayConstructor(Iterable<String> strings) {
		if (strings.size > 0) {
			return '''«
				FOR string : strings
				BEFORE 'new String[] {'
				SEPARATOR ', '
				AFTER '}'
					»«addQuotationMarks(string)»«
				ENDFOR
			»'''
		} 
		return "null"
	}
	
	private static def String addQuotationMarks(String string) {
		if (string === null || string.equals("")) {
			return ""
		} else {
			return "\"" + string + "\""
		}
	}
}
