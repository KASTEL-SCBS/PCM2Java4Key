package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.JdtAstJmlUtil;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;

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
        for (DataSet dataSet : type.getDataSets()) {
            // generate one comment for each data set
            JmlComment comment = new JmlComment(dataSet);
            addAllServicesForDataSetToJmlComment(type.getServiceTypes(), dataSet, comment);
            // if the type has any IF annotation, it will get a jml comment
            JdtAstJmlUtil.addStringToAbstractType(type.getIType(), comment.toString());
        }
    }

    private void addAllServicesForDataSetToJmlComment(List<AbstractServiceType> types, DataSet dataSet,
            JmlComment comment) {
        for (AbstractServiceType type : types) {
            type.addServicesForDataSetToJmlComment(dataSet, comment);
        }
    }
}
