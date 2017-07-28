package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.InformationFlowAnnotation.Argument;

public class JMLCommentsGenerator {

    private List<DataSet> dataSets;

    private TopLevelTypeRelations tltRelations;

    public JMLCommentsGenerator(List<DataSet> dataSets, TopLevelTypeRelations tltRelations) {
        this.dataSets = dataSets;
        this.tltRelations = tltRelations;
    }

    public void transformAllAnnotationsToJml() {
        // TODO JavaModelException
        for (TopLevelType topLevelType : tltRelations.getTopLevelTypes()) {
            try {
                transformAnnotationsToJml(topLevelType);
            } catch (JavaModelException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void transformAnnotationsToJml(TopLevelType type) throws JavaModelException {

        if (tltRelations.hasAnyIFAnnotation(type)) {

            for (DataSet dataSet : dataSets) {
                // generate one comment for each type and each data set
                JmlComment comment = new JmlComment(dataSet);

                if (tltRelations.hasProvidedTopLevelTypes(type)) {
                    checkProvidedTopLevelTypes(type, dataSet, comment);
                }

                if (tltRelations.hasRequiredTopLevelTypes(type)) {
                    checkRequiredTopLevelTypes(type, dataSet, comment);
                }
                JdtAstJmlUtil.addStringToAbstractType(type.getIType(), comment.toString());
            }
        }
    }

    private void checkProvidedTopLevelTypes(TopLevelType type, DataSet dataSet, JmlComment comment) {
        for (TopLevelType providedType : tltRelations.getProvidedTopLevelTypes(type)) {

            if (tltRelations.hasTypeSpecificationForDataSet(providedType, dataSet)) {
                List<Pair<IMethod, InformationFlowAnnotation>> methodsWithAnnotations = tltRelations
                        .getMethodWithIF(providedType);
                for (Pair<IMethod, InformationFlowAnnotation> methodWithAnnotation : methodsWithAnnotations) {
                    String service = methodWithAnnotation.getFirst().getElementName();
                    List<Argument> nonResultArguments = methodWithAnnotation.getSecond().getNonResultArguments();
                    String parameterSources = Argument.toString(nonResultArguments);
                    if (!nonResultArguments.isEmpty()) {
                        comment.addByLine(service, parameterSources);
                    }

                    List<Argument> resultArguments = methodWithAnnotation.getSecond().getResultArguments();
                    if (!resultArguments.isEmpty()) {
                        comment.addDeterminesLine(service, Argument.toString(resultArguments));
                    }
                }
            }
        }
    }

    private void checkRequiredTopLevelTypes(TopLevelType type, DataSet dataSet, JmlComment comment) {
        for (TopLevelType.Field field : tltRelations.getRequiredTopLevelTypeFields(type)) {
            TopLevelType fieldType = field.getTopLevelType();

            if (tltRelations.hasTypeSpecificationForDataSet(fieldType, dataSet)) {
                // TODO rename
                List<Pair<IMethod, InformationFlowAnnotation>> methodsWithAnnotations = tltRelations
                        .getMethodWithIF(fieldType);
                for (Pair<IMethod, InformationFlowAnnotation> methodWithAnnotation : methodsWithAnnotations) {
                    String role = field.getName();
                    String service = methodWithAnnotation.getFirst().getElementName();
                    List<Argument> nonResultArguments = methodWithAnnotation.getSecond().getNonResultArguments();
                    String parameterSources = Argument.toString(nonResultArguments);
                    if (!nonResultArguments.isEmpty()) {
                        comment.addDeterminesLine(role, service, parameterSources);
                    }

                    List<Argument> resultArguments = methodWithAnnotation.getSecond().getResultArguments();
                    if (!resultArguments.isEmpty()) {
                        comment.addByLine(role, service, Argument.toString(resultArguments));
                    }
                }
            }
        }
    }
}
