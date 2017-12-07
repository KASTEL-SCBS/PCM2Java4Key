package edu.kit.kastel.scbs.javaAnnotations2JML.type

import java.util.Scanner
import java.util.regex.Pattern

/**
 * For Modification of parameter sources, i.e. to convert an array parameter source to
 * its sequence definition.
 * 
 * @author Nils Wilka
 * @version 1.0, 06.12.2017
 */
public class ParameterSourceModification {

	private static final val String DELIMITER = "\\[\\*\\]\\."
	
	private static final val String SUFFIX = "\\[\\*\\]"
	
	/**
	 * Creates the sequence definition string for an array parameter source used in a jml contract.
	 * 
	 * Examples:
	 * 
	 * paramtersource[*]
	 * ==> (\seq_def int i; 0; paramtersource.length; paramtersource[i])
     *
     * paramtersource[*].y[*].x
     * ==> (\seq_def int i0; 0; paramtersource.length; (\seq_def int i1; 0;
     * paramtersource[i0].y.length; paramtersource[i0].y[i1].x))
	 * 
	 * @param params
	 *            The parameter source to convert to a sequence definition.
	 * 
	 * @return The sequence definition for the given parameter source.
	 */
    public static def String getSequenceDefinition(String params) {
    	val builder = new StringBuilder("")
    	val scanner = new Scanner(params)
    	scanner.useDelimiter(DELIMITER)
    	getSequenceDefinition(builder, scanner, 0)
	}
	
    private static def String getSequenceDefinition(StringBuilder prefix, Scanner scanner, int i) {
    	var token = scanner.next
    	if(scanner.hasNext) {
    		// case: params[*]. (...)
    		val builder = new StringBuilder
    		builder.append(getSequenceDefinitionPrefix(prefix.toString, token, i))
    		builder.append(getSequenceDefinition(prefix.append(layer(token, i)), scanner, i + 1))
    		builder.append(getSequenceDefinitionSuffix)
    		return builder.toString
    	} else {
    		if(Pattern.matches(".*" + SUFFIX, token)) {
	    		// case: params[*]
	    		token = removeSuffix(token, SUFFIX);
    			if(i == 0) {
    				return getSimpleSequenceDefinition(token);
    			} else {
	    			return getComplexSequenceDefinition(prefix.toString, token, i)  				
    			}
    		} else {
    			// case: params
    			'''«prefix»«token»'''    				
    		}
    	}
	}
	
    private static def String removeSuffix(String token, String suffix) {
    	// TODO change this lazy solution
    	val scanner = new Scanner(token)
    	scanner.useDelimiter(suffix)
    	return scanner.next
    }
	
    private static def String layer(String token, int i) {
    	'''«token»[i«i»].'''
    }
	
    private static def String getSequenceDefinitionPrefix(String prefix, String params, int i) {
    	'''(seq_def int i«i»; 0; «prefix»«params».length; '''
    }
    
    private static def String getSequenceDefinitionSuffix() {
    	''')'''
    }
    
    private static def String getComplexSequenceDefinition(String prefix, String params, int i) {
    	'''(seq_def int i«i»; 0; «prefix»«params».length; «prefix»«params»[i«i»])'''
    }
	
    private static def String getSimpleSequenceDefinition(String params) {
    	'''(seq_def int i; 0; «params».length; «params»[i])'''
    }
}