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
import edu.kit.kastel.scbs.confidentiality.data.ParameterizedDataSetMapEntry
import edu.kit.kastel.scbs.confidentiality.data.SpecificationParameter

final class PCM2Java4KeYGeneratorConfidentiality {
	
	private static final String PACKAGE = "confidentialityRepository"
	
	private static final String DATA_SETS_CLASS_NAME = "DataSets"
	
	private static final String DATA_SETS = '''package «PACKAGE»;
public enum «DATA_SETS_CLASS_NAME» {
EXAMPLE // TODO: verify data sets.
	
	public final String id;
	public final String name;
	
	 private «DATA_SETS_CLASS_NAME»(String id, String name) {
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
	
	private 	«DATA_SET_MAPS_CLASS_NAME»(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
'''

	private static final String DATA_SET_MAPS_EXAMPLE = '''    EXAMPLE("DSM1", "data set map 1");'''

	private static final String DATA_SET_MAP_ENTRIES_CLASS_NAME = "DataSetMapEntries"

	private static final String DATA_SET_MAP_ENTRIES = '''package «PACKAGE»;
public enum «DATA_SET_MAP_ENTRIES_CLASS_NAME» {
EXAMPLE // TODO: verify data set map entries
	
	public final String id;
	public final String name;
	public final DataSetMaps map;
	
	private «DATA_SET_MAP_ENTRIES_CLASS_NAME»(String id, String name, DataSetMaps map) {
		this.id = id;
		this.name = name;
		this.map = map;
	}
}
'''
	
	private static final String DATA_SET_MAP_ENTRIES_EXAMPLE = '''    EXAMPLE_A("DSM1_E1", "EXAMPLE[A]", null /* DataSetMaps.EXAMPLE */);'''
	
	private static final String SPECIFICATION_PARAMETERS_CLASS_NAME = "SpecificationParameters"
	
	private static final String SPECIFICATION_PARAMETERS = '''package «PACKAGE»;
	public enum «SPECIFICATION_PARAMETERS_CLASS_NAME» {
EXAMPLE // TODO: verify specification parameters
	
	public final String id;
	public final String name;
	public final String definingServiceParameter;
	
	private «SPECIFICATION_PARAMETERS_CLASS_NAME»(String id, String name, String definingServiceParameter) {
		this.id = id;
		this.name = name;
		this.definingServiceParameter = definingServiceParameter;
	}
}'''
	
	private static final String SPECIFICATION_PARAMETERS_EXAMPLE = '''    EXAMPLE("1234", "EXAMPLE", "exampleParameter");'''

	private static final String PARAMETERIZED_DATA_SET_MAP_ENTRIES_CLASS_NAME = "ParameterizedDataSetMapEntries"
	
	private static final String PARAMETERIZED_DATA_SET_MAP_ENTRIES = '''package «PACKAGE»;
public enum «PARAMETERIZED_DATA_SET_MAP_ENTRIES_CLASS_NAME» {
EXAMPLE // TODO: verify parameterized data set map entries
	
	public final String id;
	public final DataSetMaps map;
	public final SpecificationParameters parameter;
	
	private «PARAMETERIZED_DATA_SET_MAP_ENTRIES_CLASS_NAME»(String id, DataSetMaps map, SpecificationParameters parameter) {
		this.id = id;
		this.map = map;
		this.parameter = parameter;
	}
}'''
	
	private static final String PARAMETERIZED_DATA_SET_MAP_ENTRIES_EXAMPLE = '''	EXAMPLE("1234", null /* DataSetMaps.EXAMPLE */, null /* SpecificationParameters.EXAMPLE */);'''

	private static final String PARAMETERS_AND_DATA_PAIRS_CLASS_NAME = "ParametersAndDataPairs"

	private static final String PARAMETERS_AND_DATA_PAIRS = '''package «PACKAGE»;
public enum «PARAMETERS_AND_DATA_PAIRS_CLASS_NAME» {
EXAMPLE // TODO: verify parameters and data pairs
	
	public final String[] parameterSources;
	public final DataSets[] dataSets;
	public final DataSetMapEntries[] dataSetMapEntries;
	public final ParameterizedDataSetMapEntries[] parameterizedDataSetMapEntries;
	
	private «PARAMETERS_AND_DATA_PAIRS_CLASS_NAME»(String[] parameterSources, DataSets[] dataSets, DataSetMapEntries[] dataSetMapEntries, ParameterizedDataSetMapEntries[] parameterizedDataSetMapEntries) {
		this.parameterSources = parameterSources;
		this.dataSets = dataSets;
		this.dataSetMapEntries = dataSetMapEntries;
		this.parameterizedDataSetMapEntries = parameterizedDataSetMapEntries;
	}	
}
'''
	
	private static final String PARAMETERS_AND_DATA_PAIRS_EXAMPLE = '''    EXAMPLE(new String[] {param1}, null /* new DataSets[] {DataSets.EXAMPLE} /*, null, null);'''

	private static final String INFORMATION_FLOW_CLASS_NAME = "InformationFlow"

	private static final String INFORMATION_FLOW = '''package «PACKAGE»;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface «INFORMATION_FLOW_CLASS_NAME» {
	«PARAMETERS_AND_DATA_PAIRS_CLASS_NAME»[] «PARAMETERS_AND_DATA_PAIRS_CLASS_NAME.toFirstLower»();
}
'''
	
	private static final String FOLDER_NAME = "src-gen" + File.separator + "confidentialityRepository"
	
	private static final String FILE_EXT = ".java"

	static def Triplet<String, String, String>[] generateConfidentialityCodeWithFolderAndFileNames(Iterable<StereotypeApplication> stereotypeApplications) {
		val Triplet<String, String, String>[] result = newArrayOfSize(7)
		val userParametersAndDataPairs = getUserParametersAndDataPairs(stereotypeApplications)
		val userDataSets = getUserDataSets(userParametersAndDataPairs)
		val userDataSetMapEntries = getUserDataSetMapEntries(userParametersAndDataPairs)
		val userParameterizedDataSetMapEntries = getUserParameterizedDataSetMapEntries(userParametersAndDataPairs)
		val userDataSetMaps = getUserDataSetMaps(userDataSetMapEntries, userParameterizedDataSetMapEntries)
		val userSpecificationParameters = getUserSpecificationParameters(userParameterizedDataSetMapEntries)
		
		result.set(0, new Triplet(addUserDataSets(DATA_SETS, userDataSets), FOLDER_NAME, DATA_SETS_CLASS_NAME + FILE_EXT))
		result.set(1, new Triplet(addUserDataSetMaps(DATA_SET_MAPS, userDataSetMaps), FOLDER_NAME, DATA_SET_MAPS_CLASS_NAME + FILE_EXT))
		result.set(2, new Triplet(addUserDataSetMapEntries(DATA_SET_MAP_ENTRIES, userDataSetMapEntries), FOLDER_NAME, DATA_SET_MAP_ENTRIES_CLASS_NAME + FILE_EXT))
		result.set(3, new Triplet(addUserParametersAndDataPairs(PARAMETERS_AND_DATA_PAIRS, userParametersAndDataPairs), FOLDER_NAME,PARAMETERS_AND_DATA_PAIRS_CLASS_NAME + FILE_EXT))
		result.set(4, new Triplet(INFORMATION_FLOW, FOLDER_NAME, INFORMATION_FLOW_CLASS_NAME + FILE_EXT))
		result.set(5, new Triplet(addUserParameterizedDataSetMapEntries(PARAMETERIZED_DATA_SET_MAP_ENTRIES, userParameterizedDataSetMapEntries), FOLDER_NAME, PARAMETERIZED_DATA_SET_MAP_ENTRIES_CLASS_NAME + FILE_EXT))
		result.set(6, new Triplet(addUserSpecificationParameters(SPECIFICATION_PARAMETERS, userSpecificationParameters), FOLDER_NAME, SPECIFICATION_PARAMETERS_CLASS_NAME + FILE_EXT))
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
		return PACKAGE +  "."  + DATA_SET_MAP_ENTRIES_CLASS_NAME
	}
		
	static def String getParameterizedDataSetMapEntriesAsFullyQualifiedType() {
		return PACKAGE + "." + PARAMETERIZED_DATA_SET_MAP_ENTRIES_CLASS_NAME
	}
	
	static def String getParametersAndDataPairsAsFullyQualifiedType() {
		return PACKAGE +  "." + PARAMETERS_AND_DATA_PAIRS_CLASS_NAME
	}
	
	static def String getSpecificationParametersAsFullyQualifiedType() {
		return PACKAGE + "." + SPECIFICATION_PARAMETERS_CLASS_NAME
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
	
	private static def String addUserParameterizedDataSetMapEntries(String parameterizedDataSetMapEntries, Iterable<ParameterizedDataSetMapEntry> userParameterizedDataSetMapEntries) {
		if (userParameterizedDataSetMapEntries.size > 0) {
			parameterizedDataSetMapEntries.replaceFirst("EXAMPLE", userParameterizedDataSetMapEntries.generateUserParameterizedDataSetMapEntriesString)
		} else {
			return parameterizedDataSetMapEntries.replaceFirst("EXAMPLE", PARAMETERIZED_DATA_SET_MAP_ENTRIES_EXAMPLE)
		}
	}
	
	private static def String addUserParametersAndDataPairs(String parametersAndDataPairs, Iterable<ParametersAndDataPair> userParametersAndDataPairs) {
		if (userParametersAndDataPairs.size > 0) {
			parametersAndDataPairs.replaceFirst("EXAMPLE", userParametersAndDataPairs.generateUserParametersAndDataPairsString)
		} else {
			return parametersAndDataPairs.replaceFirst("EXAMPLE", PARAMETERS_AND_DATA_PAIRS_EXAMPLE)
		}
	}
	
	private static def String addUserSpecificationParameters(String specificationParameters, Iterable<SpecificationParameter> userSpecificationParameters) {
		if (userSpecificationParameters.size > 0) {
			specificationParameters.replaceFirst("EXAMPLE", userSpecificationParameters.generateUserSpecificationParametersString)
		} else {
			return specificationParameters.replaceFirst("EXAMPLE", SPECIFICATION_PARAMETERS_EXAMPLE)
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
		»«dataSetMap.name.ConvertToEnumValueName»("«dataSetMap.id»", "«dataSetMap.name»")«
		ENDFOR»
	''' 
	
	private static def String generateUserDataSetMapEntriesString(Iterable<DataSetMapEntry> userDataSetMapEntries)'''«
		FOR dataSetMapEntry : userDataSetMapEntries
		BEFORE "	"
		SEPARATOR "," + newLine +  "	"    
		AFTER ";"
		»«dataSetMapEntry.name.ConvertToEnumValueName»("«dataSetMapEntry.id»", "«dataSetMapEntry.name»", DataSetMaps.«dataSetMapEntry.map.name.ConvertToEnumValueName»)«
		ENDFOR»
	''' 
	
	private static def String generateUserParameterizedDataSetMapEntriesString(Iterable<ParameterizedDataSetMapEntry> userParameterizedDataSetMapEntries)'''«
		FOR pdsme : userParameterizedDataSetMapEntries
		BEFORE "	"
		SEPARATOR "," + newLine +  "	"    
		AFTER ";"
		»«pdsme.map.name.ConvertToEnumValueName»_«pdsme.parameter.definingServiceParameter.ConvertToEnumValueName»("«pdsme.id»", DataSetMaps.«pdsme.map.name.ConvertToEnumValueName», SpecificationParameters.«pdsme.parameter.name.ConvertToEnumValueName»)«
		ENDFOR»
	''' 
	
	private static def String generateUserParametersAndDataPairsString(Iterable<ParametersAndDataPair> userParametersAndDataPairs)'''«
		FOR parametersAndDataPair : userParametersAndDataPairs
		BEFORE "	"
		SEPARATOR "," + newLine +  "	"    
		AFTER ";"
		»«parametersAndDataPair.name»(«parametersAndDataPair.parameterSources.generateStringArrayConstructor», «parametersAndDataPair.dataTargets.filter(DataSet).generateDataSetArrayConstructor», «
		  parametersAndDataPair.dataTargets.filter(DataSetMapEntry).generateDataSetMapEntryArrayConstructor», «parametersAndDataPair.dataTargets.filter(ParameterizedDataSetMapEntry).generateParameterizedDataSetMapEntryArrayConstructor»)«
	ENDFOR»''' 
	
	private static def String generateUserSpecificationParametersString(Iterable<SpecificationParameter> userSpecificationParameters)'''«
		FOR specificationParameter : userSpecificationParameters
		BEFORE "	"
		SEPARATOR "," + newLine +  "	"    
		AFTER ";"
		»«specificationParameter.name.ConvertToEnumValueName»("«specificationParameter.id»", "«specificationParameter.name»", "«specificationParameter.definingServiceParameter»")«
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
	
	private static def String generateParameterizedDataSetMapEntryArrayConstructor(Iterable<ParameterizedDataSetMapEntry> parameterizedDataSetMapEntries) {
		if (parameterizedDataSetMapEntries.size > 0) {
			return '''«
				FOR pdsme : parameterizedDataSetMapEntries
				BEFORE 'new ParameterizedDataSetMapEntries[] {'
				SEPARATOR ', '
				AFTER '}'
					»ParameterizedDataSetMapEntries.«pdsme.map.name.ConvertToEnumValueName»_«pdsme.parameter.definingServiceParameter.ConvertToEnumValueName»«
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
	
	private static def DataSetMap[] getUserDataSetMaps(Iterable<DataSetMapEntry> dataSetMapEntries, Iterable<ParameterizedDataSetMapEntry> parameterizedDataSetMapEntries) {
		val dataSetMaps = dataSetMapEntries.map[it.getMap].toSet
		dataSetMaps.addAll(parameterizedDataSetMapEntries.map[it.getMap].toSet)
		return dataSetMaps
	}
	
	private static def DataSetMapEntry[] getUserDataSetMapEntries(Iterable<ParametersAndDataPair> parametersAndDataPairs) {
		parametersAndDataPairs.map[it.dataTargets].flatten.filter(DataSetMapEntry).toSet
	} 
	
	private static def ParameterizedDataSetMapEntry[] getUserParameterizedDataSetMapEntries(Iterable<ParametersAndDataPair> parametersAndDataPairs) {
		parametersAndDataPairs.map[it.dataTargets].flatten.filter(ParameterizedDataSetMapEntry).toSet
	}
	
	private static def SpecificationParameter[] getUserSpecificationParameters(Iterable<ParameterizedDataSetMapEntry> parameterizedDataSetMapEntries) {
		parameterizedDataSetMapEntries.map[it.parameter].toSet
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
