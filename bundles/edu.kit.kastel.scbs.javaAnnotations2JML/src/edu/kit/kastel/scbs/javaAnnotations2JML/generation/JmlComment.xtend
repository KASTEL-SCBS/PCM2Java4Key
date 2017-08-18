package edu.kit.kastel.scbs.javaAnnotations2JML.generation

import java.util.List
import java.util.LinkedList
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet

/**
 * Represents a jml comment for information flow properties in a java type.
 * 
 * The string representation is available via the {@code toString} method.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
class JmlComment {

	private String dataSet;

	private List<String> determinesList;

	private List<String> byList;

	/**
	 * Creates a new jml comment with the given data set name.
	 * 
	 * @param dataSet The data set name.
	 */
	public new(DataSet dataSet) {
		this.dataSet = dataSet.getName();
		this.determinesList = new LinkedList();
		this.byList = new LinkedList();
	}
	
	/**
	 * Adds the given service name and parameter sources string to the "determines" keyword with the self reference as role.
	 * 
	 * @param service The service name.
	 * 
	 * @param service The parameter sources string.
	 */
	def public void addDeterminesLine(String service, String parameterSources) {
		var result = "this" + "." + service + "(" + parameterSources + ")";
		determinesList.add(result);
	}

	/**
	 * Adds the given role name, service name and parameter sources string to the "determines".
	 * 
	 * @param role The role name.
	 * 
	 * @param service The service name.
	 * 
	 * @param service The parameter sources string.
	 */
	def public void addDeterminesLine(String role, String service, String parameterSources) {
		var result = role + "." + service + "(" + parameterSources + ")";
		determinesList.add(result);
	}
	
	/**
	 * Adds the given service name and parameter sources string to the "by" keyword with the self reference as role.
	 * 
	 * @param service The service name.
	 * 
	 * @param service The parameter sources string.
	 */
	def public void addByLine(String service, String parameterSources) {
		var result = "this" + "." + service + "(" + parameterSources + ")";
		byList.add(result);
	}

	/**
	 * Adds the given role name, service name and parameter sources string to the "by" keyword.
	 * 
	 * @param role The role name.
	 * 
	 * @param service The service name.
	 * 
	 * @param service The parameter sources string.
	 */
	def public void addByLine(String role, String service, String parameterSources) {
		var result = role + "." + service + "(" + parameterSources + ")";
		byList.add(result);
	}

	/**
	 * Creates the string representation of the given list.
	 * 
	 * @return The string representation of the given list.
	 * 
	 * @param list The list of strings to be converted.
	 */
	def private String getString(List<String> list) {
		if (list.empty) {
			return "\\nothing"
		} else {
			var result = list.head;
				
			if (list.size > 2) {
				result += ",\n"
				for (var i = 1; i < list.length - 1; i++) {
					result += newLine(list.get(i)) + ",\n"
				}
				result += newLine(list.last)
			} else if (list.size > 1) {
				result += ",\n" + newLine(list.last)
			}
			return result;
		}
	}

	/**
	 * Creates a new line for an jml comment with the given content.
	 * 
	 * @return A new jml line with the given content.
	 * 
	 * @param list The content of the line to be created.
	 */
	def private newLine(String content) {
		'''//@     «content»'''
	}

	override public toString() {
		'''
		//automatically generated:
		//@ model \seq «dataSet»;
		//
		//@ \determines «getString(determinesList)»
		//@ \by «getString(byList)»
		//@ \preserving «dataSet»;'''
	}
}
