package edu.kit.kastel.scbs.javaAnnotations2JML.type

import java.util.List
import java.util.LinkedList
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet

/**
 * Represents a java comment block in the java modeling language for information flow properties in a java type.
 * 
 * The string representation is available via the {@code toString} method.
 * 
 * @author Nils Wilka
 * @version 2.0, 17.09.2017
 */
class JmlComment {
	
	private final static String CALL = "\\call";
	
	private final static String TERM = "\\term";

	private final String dataSet;
	
	private final List<String> visibleList;

	private final List<String> lowOutList;

	private final List<String> lowInList;

	/**
	 * Creates a new jml comment with the given data set name.
	 * 
	 * @param dataSet The data set name.
	 */
	public new(DataSet dataSet) {
		this.dataSet = dataSet.getName();
		this.lowOutList = new LinkedList();
		this.lowInList = new LinkedList();
		this.visibleList = new LinkedList();
	}
	
	/**
	 * Adds the given service name string to the "visible" keyword with the self reference as role.
	 * 
	 * @param service The service name.
	 */
	def public void addVisible(String service) {
		addVisible("this", service);
	}
	
	/**
	 * Adds the given service name string to the "visible" keyword.
	 * 
	 * @param role The role name.
	 * 
	 * @param service The service name.
	 */
	def public void addVisible(String role, String service) {
		visibleList.add(buildServiceString(role, service, CALL, "true"));
		visibleList.add(buildServiceString(role, service, TERM, "true"));
	}
	
	/**
	 * Adds the given service name and parameter sources string to the "lowout" keyword with the self reference as role.
	 * 
	 * @param service The service name.
	 * 
	 * @param parameterSources The parameter sources string.
	 */
	def public void addLowOut(String service, String parameterSources) {
		addLowOut("this", service, TERM, parameterSources);
	}
	
	/**
	 * Adds the given role name, service name and parameter sources string to the "lowout".
	 * 
	 * @param role The role name.
	 * 
	 * @param service The service name.
	 * 
	 * @param parameterSources The parameter sources string.
	 */
	def public void addLowOut(String role, String service, String parameterSources) {
		lowOutList.add(buildServiceString(role, service, CALL, parameterSources));
	}

	/**
	 * Adds the given role name, service name and parameter sources string to the "lowout".
	 * 
	 * @param role The role name.
	 * 
	 * @param service The service name.
	 * 
	 * @param type Either {@code CALL} or {@code TERM}.
	 * 
	 * @param parameterSources The parameter sources string.
	 */
	def private void addLowOut(String role, String service, String type, String parameterSources) {
		lowOutList.add(buildServiceString(role, service, type, parameterSources));
	}
	
	/**
	 * Adds the given service name and parameter sources string to the "lowin" keyword with the self reference as role.
	 * 
	 * @param service The service name.
	 * 
	 * @param parameterSources The parameter sources string.
	 */
	def public void addLowIn(String service, String parameterSources) {
		addLowIn("this", service, CALL, parameterSources);
	}
	
	/**
	 * Adds the given role name, service name and parameter sources string to the "lowin" keyword.
	 * 
	 * @param role The role name.
	 * 
	 * @param service The service name.
	 * 
	 * @param parameterSources The parameter sources string.
	 */
	def public void addLowIn(String role, String service, String parameterSources) {
		lowInList.add(buildServiceString(role, service, TERM, parameterSources));
	}

	/**
	 * Adds the given role name, service name and parameter sources string to the "lowin" keyword.
	 * 
	 * @param role The role name.
	 * 
	 * @param service The service name.
	 * 
	 * @param type Either {@code CALL} or {@code TERM}.
	 * 
	 * @param parameterSources The parameter sources string.
	 */ 
	def private void addLowIn(String role, String service, String type, String parameterSources) {
		lowInList.add(buildServiceString(role, service, type, parameterSources));
	}
	
	/**
	 * Creates a service string from the given role name, service name, type and parameter sources string.
	 * 
	 * @param role The role name.
	 * 
	 * @param service The service name.
	 * 
	 * @param type Either {@code CALL} or {@code TERM}.
	 * 
	 * @param parameterSources The parameter sources string.
	 * 
	 * @return The service string with the given information.
	 */ 
	def private String buildServiceString(String role, String service, String type, String parameterSources) {
		'''«role».«service».«type»(«parameterSources»)'''
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
	 * @param list The content of the line to be created.
	 * 
	 * @return A new jml line with the given content.
	 */
	def private newLine(String content) {
		'''//@     «content»'''
	}

	override public toString() {
		'''
		//automatically generated:
		//@ cluster «dataSet»Cluster
		//@ \lowIn «getString(lowInList)»
		//@ \lowOut «getString(lowOutList)»
		//@ \visible «getString(visibleList)»
		//@ \lowState «dataSet»;
		//@ 
		//@ model \seq «dataSet»;
		    
		'''
	}
}
