package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
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
            try {
                // generate comments for each type
                transformAnnotationsToJml(topLevelType);
            } catch (JavaModelException jme) {
                Optional<String> message = Optional.ofNullable(jme.getMessage());
                throw new IOException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
            }
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
     * @throws JavaModelException
     *             if jml comment could not be written to type.
     */
    public void transformAnnotationsToJml(TopLevelType type) throws JavaModelException {
        // do not generate comments for classes without specification
        for (DataSet dataSet : type.getServiceTypeDataSets()) {
            // generate one comment for each data set
            JmlComment comment = new JmlComment(dataSet);
            addAllServicesForDataSetToJmlComment(type.getServiceTypes(), dataSet, comment);
            // if the type has any IF annotation, it will get a jml comment
            try {
                JdtAstJmlUtil.addStringToAbstractType(type.getIType(), comment.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds the services of each abstract service type to the given jml comment.
     * 
     * Only adds the services for the given data set.
     * 
     * @param types
     *            The abstract service types to get the services from and adding them to the
     *            comment.
     * @param dataSet
     *            The data set to get the services for.
     * @param comment
     *            The comment to add to.
     */
    private void addAllServicesForDataSetToJmlComment(List<AbstractServiceType> types, DataSet dataSet,
            JmlComment comment) {
        for (AbstractServiceType type : types) {
            type.addServicesForDataSetToJmlComment(dataSet, comment);
        }
    }
}
