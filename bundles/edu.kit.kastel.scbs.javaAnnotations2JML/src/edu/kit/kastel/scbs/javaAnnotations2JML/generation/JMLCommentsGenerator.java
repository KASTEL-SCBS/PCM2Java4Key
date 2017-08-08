package edu.kit.kastel.scbs.javaAnnotations2JML.generation;

import java.io.IOException;
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
        if (topLevelTypeMappings.hasAnyIFAnnotation(type)) {
            for (DataSet dataSet : specification.getDataSets()) {
                // generate one comment for each type and each data set
                JmlComment comment = new JmlComment(dataSet);
                // no iteration over empty provided / required lists
                scanProvidedTopLevelTypes(type, dataSet, comment);
                scanRequiredTopLevelTypes(type, dataSet, comment);
                // if the type has any IF annotation, it will get a jml comment
                JdtAstJmlUtil.addStringToAbstractType(type.getIType(), comment.toString());
            }
        }
    }

    private void scanProvidedTopLevelTypes(TopLevelType type, DataSet dataSet, JmlComment comment) {
        for (TopLevelType providedType : topLevelTypeMappings.getProvidedTopLevelTypes(type)) {
            if (topLevelTypeMappings.hasTypeSpecificationForDataSet(providedType, dataSet)) {
                List<Pair<IMethod, InformationFlowAnnotation>> methodsWithAnnotations = topLevelTypeMappings
                        .getMethodWithIF(providedType);
                for (Pair<IMethod, InformationFlowAnnotation> methodAnnotationPair : methodsWithAnnotations) {
                    String name = methodAnnotationPair.getFirst().getElementName();
                    InformationFlowAnnotation annotation = methodAnnotationPair.getSecond();
                    AbstractService service = new ProvidedService(name, annotation.getParameterSources());
                    service.addServiceToJmlComment(comment);
                }
            }
        }
    }

    private void scanRequiredTopLevelTypes(TopLevelType type, DataSet dataSet, JmlComment comment) {
        for (TopLevelType.Field field : topLevelTypeMappings.getRequiredTopLevelTypeFields(type)) {
            TopLevelType fieldType = field.getTopLevelType();
            String role = field.getName();
            if (topLevelTypeMappings.hasTypeSpecificationForDataSet(fieldType, dataSet)) {
                List<Pair<IMethod, InformationFlowAnnotation>> methodsWithAnnotations = topLevelTypeMappings
                        .getMethodWithIF(fieldType);
                for (Pair<IMethod, InformationFlowAnnotation> methodAnnotationPair : methodsWithAnnotations) {
                    String name = methodAnnotationPair.getFirst().getElementName();
                    InformationFlowAnnotation annotation = methodAnnotationPair.getSecond();
                    AbstractService service = new RequiredService(role, name, annotation.getParameterSources());
                    service.addServiceToJmlComment(comment);
                }
            }
        }
    }
}
