package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParametersAndDataPair;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParametersAndDataPair.ParameterSource;

public class JMLCommentsGenerator {

    private ConfidentialitySpecification specification;

    private TopLevelTypeMappings topLevelTypeMappings;

    public JMLCommentsGenerator(ConfidentialitySpecification specification, TopLevelTypeMappings topLevelTypeMappings) {
        this.specification = specification;
        this.topLevelTypeMappings = topLevelTypeMappings;
    }

    public void transformAllAnnotationsToJml() {
        // TODO JavaModelException
        for (TopLevelType topLevelType : topLevelTypeMappings.getTopLevelTypes()) {
            try {
                transformAnnotationsToJml(topLevelType);
            } catch (JavaModelException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void transformAnnotationsToJml(TopLevelType type) throws JavaModelException {
        // do not generate comments for classes without specification
        if (topLevelTypeMappings.hasAnyIFAnnotation(type)) {
            for (DataSet dataSet : specification.getDataSets()) {
                // generate one comment for each type and each data set
                JmlComment comment = new JmlComment(dataSet);

                if (topLevelTypeMappings.hasProvidedTopLevelTypes(type)) {
                    checkProvidedTopLevelTypes(type, dataSet, comment);
                }

                if (topLevelTypeMappings.hasRequiredTopLevelTypes(type)) {
                    checkRequiredTopLevelTypes(type, dataSet, comment);
                }
                JdtAstJmlUtil.addStringToAbstractType(type.getIType(), comment.toString());
            }
        }
    }

    private void checkProvidedTopLevelTypes(TopLevelType type, DataSet dataSet, JmlComment comment) {
        for (TopLevelType providedType : topLevelTypeMappings.getProvidedTopLevelTypes(type)) {

            if (topLevelTypeMappings.hasTypeSpecificationForDataSet(providedType, dataSet)) {
                List<Pair<IMethod, InformationFlowAnnotation>> methodsWithAnnotations = topLevelTypeMappings
                        .getMethodWithIF(providedType);
                for (Pair<IMethod, InformationFlowAnnotation> methodAnnotationPair : methodsWithAnnotations) {
                    IMethod method = methodAnnotationPair.getFirst();
                    InformationFlowAnnotation annotation = methodAnnotationPair.getSecond();

                    String service = method.getElementName();
                    List<ParameterSource> nonResultParameterSources = getNonResultParameterSources(annotation);
                    if (!nonResultParameterSources.isEmpty()) {
                        String parameterSourcesString = ParameterSource.toString(nonResultParameterSources);
                        comment.addByLine(service, parameterSourcesString);
                    }

                    List<ParameterSource> resultParameterSources = getResultParameterSources(annotation);
                    if (!resultParameterSources.isEmpty()) {
                        comment.addDeterminesLine(service, ParameterSource.toString(resultParameterSources));
                    }
                }
            }
        }
    }

    private void checkRequiredTopLevelTypes(TopLevelType type, DataSet dataSet, JmlComment comment) {
        for (TopLevelType.Field field : topLevelTypeMappings.getRequiredTopLevelTypeFields(type)) {
            TopLevelType fieldType = field.getTopLevelType();

            if (topLevelTypeMappings.hasTypeSpecificationForDataSet(fieldType, dataSet)) {
                // TODO rename
                List<Pair<IMethod, InformationFlowAnnotation>> methodsWithAnnotations = topLevelTypeMappings
                        .getMethodWithIF(fieldType);
                for (Pair<IMethod, InformationFlowAnnotation> methodAnnotationPair : methodsWithAnnotations) {
                    IMethod method = methodAnnotationPair.getFirst();
                    InformationFlowAnnotation annotation = methodAnnotationPair.getSecond();

                    String role = field.getName();
                    String service = method.getElementName();
                    List<ParameterSource> nonResultParameterSources = getNonResultParameterSources(annotation);
                    if (!nonResultParameterSources.isEmpty()) {
                        String parameterSourcesString = ParameterSource.toString(nonResultParameterSources);
                        comment.addDeterminesLine(role, service, parameterSourcesString);
                    }

                    List<ParameterSource> resultParameterSources = getResultParameterSources(annotation);
                    if (!resultParameterSources.isEmpty()) {
                        comment.addByLine(role, service, ParameterSource.toString(resultParameterSources));
                    }
                }
            }
        }
    }

    private List<ParameterSource> getNonResultParameterSources(InformationFlowAnnotation annotation) {
        return ParametersAndDataPair.getAllNonResultParameterSources(annotation.getParameterAndDataPairs());
    }

    private List<ParameterSource> getResultParameterSources(InformationFlowAnnotation annotation) {
        return ParametersAndDataPair.getAllResultParameterSources(annotation.getParameterAndDataPairs());
    }
}
