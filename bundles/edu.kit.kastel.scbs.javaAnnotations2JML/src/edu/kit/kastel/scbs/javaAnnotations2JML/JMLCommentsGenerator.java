package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class JMLCommentsGenerator {

    private List<DataSet> dataSets;

    private List<TopLevelType> topLevelTypes;

    public JMLCommentsGenerator(List<DataSet> dataSets, List<TopLevelType> topLevelTypes) {
        this.dataSets = dataSets;
        this.topLevelTypes = topLevelTypes;
    }

    public void transformAllAnnotationsToJml() {
        // TODO JavaModelException
        for (TopLevelType topLevelType : topLevelTypes) {
            try {
                transformAnnotationsToJml(topLevelType);
            } catch (JavaModelException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void transformAnnotationsToJml(TopLevelType type) throws JavaModelException {
        List<TopLevelType.Field> requiredTopLevelTypeFields = type.getRequiredTopLevelTypeFields();
        List<TopLevelType> providedTopLevelTypes = type.getProvidedTopLevelTypes();

        if (!requiredTopLevelTypeFields.isEmpty() || !providedTopLevelTypes.isEmpty()) {
            // TODO WIP
            for (DataSet dataSet : dataSets) {
                JmlComment comment = new JmlComment(dataSet);

                for (TopLevelType.Field field : requiredTopLevelTypeFields) {
                    TopLevelType fieldType = field.getTopLevelType();
                    HashMap<IMethod, String> method2ParameterSourcesMap = fieldType
                            .getMethodParameterSourcesPairs(dataSet);
                    for (IMethod method : method2ParameterSourcesMap.keySet()) {
                        String role = field.getName();
                        String service = method.getElementName();
                        String parameterSources = method2ParameterSourcesMap.get(method);
                        comment.addDeterminesLine(role, service, parameterSources);
                    }
                }

                for (TopLevelType providedType : providedTopLevelTypes) {
                    HashMap<IMethod, String> method2ParameterSourcesMap = providedType
                            .getMethodParameterSourcesPairs(dataSet);
                    for (IMethod method : method2ParameterSourcesMap.keySet()) {
                        String service = method.getElementName();
                        String parameterSources = method2ParameterSourcesMap.get(method);
                        comment.addByLine(service, parameterSources);
                    }
                }

                JdtAstJmlUtil.addStringToAbstractType(type.getIType(), comment.toString());
            }
        }
    }
}
