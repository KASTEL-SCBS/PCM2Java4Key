package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.JdtAstJmlUtil;

public class JMLCommentsGenerator {

    private List<TopLevelType> topLevelTypes;

    public JMLCommentsGenerator(List<TopLevelType> topLevelTypes) {
        this.topLevelTypes = topLevelTypes;
    }

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

    public void transformAnnotationsToJml(TopLevelType type) throws JavaModelException {
        // do not generate comments for classes without specification
        for (DataSet dataSet : type.getRelatedDataSets()) {
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

    private void addAllServicesForDataSetToJmlComment(List<AbstractServiceType> types, DataSet dataSet,
            JmlComment comment) {
        for (AbstractServiceType type : types) {
            type.addServicesForDataSetToJmlComment(dataSet, comment);
        }
    }
}
