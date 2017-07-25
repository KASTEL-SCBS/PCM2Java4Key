package edu.kit.kastel.scbs.javaAnnotations2JML

import java.util.List
import java.util.LinkedList

class JmlComment {

	private String dataSet;

	private List<String> determinesList;

	private List<String> byList;

	public new() {
		this.dataSet = "\\nothing";
		this.determinesList = new LinkedList();
		this.byList = new LinkedList();
	}

	public new(DataSet dataSet) {
		this.dataSet = dataSet.getName();
		this.determinesList = new LinkedList();
		this.byList = new LinkedList();
	}

	def public String getDataSet() {
		return dataSet
	}

	def public void setDataSet(String dataSet) {
		this.dataSet = dataSet
	}

	def public List<String> getDeterminesList() {
		return determinesList;
	}

	def public void addDeterminesLine(String line) {
		determinesList.add(line);
	}

	def public void setDeterminesList(List<String> list) {
		this.determinesList = list
	}

	def public List<String> getByList() {
		return byList
	}

	def public void addByLine(String line) {
		determinesList.add(line);
	}

	def public void setByList(List<String> list) {
		this.byList = byList
	}

	def public String getString(List<String> list) {
		if (list.empty) {
			return "\\nothing"
		} else {
			var result = list.head;
				
			if (list.size > 2) {
				result += ",\n"
				for (var i = 1; i < list.length - 1; i++) {
					result += line(list.get(i)) + ",\n"
				}
				result += line(list.last)
			} else if (list.size > 1) {
				result += ",\n" + line(list.last)
			}
			return result;
		}
	}

	def private line(String content) {
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
