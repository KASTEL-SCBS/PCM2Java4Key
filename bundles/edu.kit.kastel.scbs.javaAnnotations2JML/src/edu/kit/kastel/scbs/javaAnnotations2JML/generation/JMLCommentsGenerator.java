package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.JdtAstJmlUtil;

/**
 * Generates the jml comments for the given list of top level types.
 * 
 * The jml comments are used for verifying the confidentiality of information with KeY.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class JMLCommentsGenerator {

    private List<TopLevelType> topLevelTypes;

    /**
     * Creates a new jml comments generator with the given top level types.
     * 
     * @param topLevelTypes
     *            The top level types to generate jml comments for.
     */
    public JMLCommentsGenerator(List<TopLevelType> topLevelTypes) {
        this.topLevelTypes = topLevelTypes;
    }

    /**
     * Main method to start the jml generation.
     * 
     * @throws IOException
     *             if jml comments could not be written to type.
     */
    public void transformAllAnnotationsToJml() throws IOException {
        for (TopLevelType topLevelType : topLevelTypes) {
            // generate comments for each type
            transformAnnotationsToJml(topLevelType);
        }
    }

    /**
     * Generates the jml comments for the given {@code TopLevelType}. There is a jml comment
     * generated for each data set associated with the type.
     * 
     * The content of the jml comment is specified by the {@code AbstractServiceType}s of the type
     * and its services.
     * 
     * @param type
     *            The type to generate jml for.
     * @throws IOException
     *             if jml comment could not be written to type.
     */
    public void transformAnnotationsToJml(TopLevelType type) throws IOException {
        // do not generate comments for classes without specification
        for (DataSet dataSet : type.getServiceTypeDataSets()) {
            // generate one comment for each data set
            JmlComment comment = new JmlComment(dataSet);
            // Add the services of each abstract service type to the given jml comment.
            // Only adds the services for the given data set
            type.getServiceTypes().forEach(e -> e.addServicesForDataSetToJmlComment(dataSet, comment));
            // if the type has any IF annotation, it will get a jml comment
            try {
                JdtAstJmlUtil.addStringToAbstractType(type.getIType(), comment.toString());
            } catch (JavaModelException jme) {
                throw new IOException(jme);
            }
        }
    }
}
