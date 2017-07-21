package edu.kit.kastel.scbs.javaAnnotations2JML

import org.eclipse.jdt.core.IMethod
import edu.kit.kastel.scbs.javaAnnotations2JML.Anno2JmlUtil

class JMLCommentsGenerator {
	
    def static toJML(IMethod toTransform) {
    	'''
		//This is a dummy JML-comment:
		
		//@ model \seq «Anno2JmlUtil.getDataSet(toTransform)»;
		//
		//@ \determines 'low outputs'
		//@ \by this.orderElementsInCart(\call), 
		//@     this.setName(\call, name.length, name[*]),
		//@     this.setAdress(\call, address.length, address[*]),
		//@     bank.makePayment(\result)
		//@ \preserving «Anno2JmlUtil.getDataSet(toTransform)»;'''
    }
	
}
