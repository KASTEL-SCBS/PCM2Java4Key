package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.JdtAstJmlUtil;
import edu.kit.kastel.scbs.javaAnnotations2JML.Pair;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelTypeMappings;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

public class JMLCommentsGenerator {

    private ConfidentialitySpecification specification;

    private TopLevelTypeMappings topLevelTypeMappings;

    public JMLCommentsGenerator(ConfidentialitySpecification specification, TopLevelTypeMappings topLevelTypeMappings) {
        this.specification = specification;
        this.topLevelTypeMappings = topLevelTypeMappings;
    }

    public void transformAllAnnotationsToJml() throws IOException {
        for (TopLevelType topLevelType : topLevelTypeMappings.getTopLevelTypes()) {
            try {
                transformAnnotationsToJml(topLevelType);
            } catch (JavaModelException jme) {
                Optional<String> message = Optional.ofNullable(jme.getMessage());
                throw new IOException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
            }
        }
    }

    public void transformAnnotationsToJml(TopLevelType type) throws JavaModelException {
        // do not generate comments for classes without specification
        // TODO remove outer if
        if (topLevelTypeMappings.hasAnyIFAnnotation(type)) {
            // TODO mappings.getDataSets(type) - iterate over those
            for (DataSet dataSet : specification.getDataSets()) {
                // generate one comment for each type and each data set
                JmlComment comment = new JmlComment(dataSet);
                // create provided and required types and their services
                List<AbstractTopLevelType> abstractTypes = new LinkedList<>();
                abstractTypes.addAll(scanProvidedTopLevelTypes(type));
                abstractTypes.addAll(scanRequiredTopLevelTypes(type));
                // no iteration over empty provided / required lists
                addAllServicesForDataSetToJmlComment(abstractTypes, dataSet, comment);
                // if the type has any IF annotation, it will get a jml comment
                JdtAstJmlUtil.addStringToAbstractType(type.getIType(), comment.toString());
            }
        }
    }

    private List<AbstractTopLevelType> scanProvidedTopLevelTypes(TopLevelType type) {
        List<AbstractTopLevelType> providedTypes = new LinkedList<>();
        for (TopLevelType providedType : topLevelTypeMappings.getProvidedTopLevelTypes(type)) {
            AbstractTopLevelType abstractType = new ProvidedTopLevelType(providedType);
            addServices(providedType, abstractType);
            providedTypes.add(abstractType);
        }
        return providedTypes;
    }

    private List<AbstractTopLevelType> scanRequiredTopLevelTypes(TopLevelType type) {
        List<AbstractTopLevelType> requiredTypes = new LinkedList<>();
        for (TopLevelType.Field field : topLevelTypeMappings.getRequiredTopLevelTypeFields(type)) {
            AbstractTopLevelType abstractType = new RequiredTopLevelType(field.getName(), type);
            addServices(field.getTopLevelType(), abstractType);
            requiredTypes.add(abstractType);
        }
        return requiredTypes;
    }

    private void addServices(TopLevelType serviceType, AbstractTopLevelType abstractType) {
        for (Pair<IMethod, InformationFlowAnnotation> pair : topLevelTypeMappings.getMethodWithIF(serviceType)) {
            abstractType.addService(pair.getFirst(), pair.getSecond());
        }
    }

    private void addAllServicesForDataSetToJmlComment(List<AbstractTopLevelType> types, DataSet dataSet, JmlComment comment) {
        for (AbstractTopLevelType type : types) {
            type.addServicesForDataSetToJmlComment(dataSet, comment);
        }
    }
}
