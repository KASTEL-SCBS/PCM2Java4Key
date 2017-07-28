package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.Anno2JmlUtil;
import edu.kit.kastel.scbs.javaAnnotations2JML.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.Pair;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelTypeRelations;

public class DataSet2AnnotationsParser
        extends JavaAnnotations2JMLParser<Pair<List<DataSet>, TopLevelTypeRelations>, TopLevelTypeRelations> {

    public DataSet2AnnotationsParser(Pair<List<DataSet>, TopLevelTypeRelations> source) {
        super(source);
    }

    @Override
    public TopLevelTypeRelations parse() {
        TopLevelTypeRelations tltRelations = getSource().getSecond();
        try {
            setMaps(tltRelations);
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setResult(tltRelations);
        return getResult();
    }

    private void setMaps(TopLevelTypeRelations tltRelations) throws JavaModelException {
        Set<TopLevelType> merge = new HashSet<>();
        merge.addAll(getProvidedTopLevelTypes());
        merge.addAll(getRequiredTopLevelTypes());
        for (TopLevelType type : merge) {
            IMethod[] methods = type.getIType().getMethods();
            for (IMethod method : methods) {
                // TODO method might not have information flow property or parameter sources
                // argument
                if (Anno2JmlUtil.hasInformationFlowAnnotation(method)) {
                    Pair<String, String> pair = getMethodParameterSourcesPair(method);
                    DataSet dataSet = dataSetFromString(pair.getSecond());
                    List<InformationFlowAnnotation.Argument> arguments = InformationFlowAnnotation.Argument
                            .create(pair.getFirst());
                    InformationFlowAnnotation annotation = new InformationFlowAnnotation(dataSet, arguments);
                    tltRelations.addMethodWithIFToTopLevelType(type, method, annotation);
                    tltRelations.addTopLevelTypeAsSpecificationForDataSet(dataSet, type);
                }
            }
        }
    }

    private DataSet dataSetFromString(String dataSetId) {
        DataSet newDataSet = null;

        for (DataSet dataSet : getDataSets()) {
            if (dataSet.getId().equals(dataSetId)) {
                newDataSet = dataSet;
            }
        }
        return newDataSet;
    }

    private List<DataSet> getDataSets() {
        return getSource().getFirst();
    }

    private Set<TopLevelType> getRequiredTopLevelTypes() {
        return getSource().getSecond().getRequiredTopLevelTypes();
    }

    private Set<TopLevelType> getProvidedTopLevelTypes() {
        return getSource().getSecond().getProvidedTopLevelTypes();
    }

    public Pair<String, String> getMethodParameterSourcesPair(IMethod method) throws JavaModelException {
        String parameterSources = "";
        String dataSets = "";

        for (IMemberValuePair pair : Anno2JmlUtil.getIFAnnotationArguments(method)) {
            String memberName = pair.getMemberName();
            Object[] o = (Object[]) pair.getValue();
            if (memberName.equals("parameterSources")) {
                parameterSources = (String) o[0];
            } else if (memberName.equals("dataSets")) {
                dataSets = (String) o[0];
            }
            // TODO else
        }
        return new Pair<>(parameterSources, dataSets);
    }
}
