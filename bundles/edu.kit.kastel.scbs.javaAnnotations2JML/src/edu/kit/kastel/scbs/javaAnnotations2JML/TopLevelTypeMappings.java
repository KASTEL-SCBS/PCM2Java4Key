package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;

public class TopLevelTypeMappings {

    /* top level types of the project */
    private List<TopLevelType> topLevelTypes;

    /* for parsing once for information flow properties */

    // provided top level types image set
    private Set<TopLevelType> providedTypes;

    // required top level types image set
    private Set<TopLevelType> requiredTypes;

    /* mapping for each top level type in the project to its provided/required types/fields */

    private Map<TopLevelType, List<TopLevelType>> tlt2providedTopLevelTypes;

    private Map<TopLevelType, List<TopLevelType.Field>> tlt2requiredTopLevelTypeFields;

    private Map<TopLevelType, List<Pair<IMethod, InformationFlowAnnotation>>> topLevelType2MethodWithIF;

    private Map<DataSet, Set<TopLevelType>> dataSet2TopLevelTypes;

    public TopLevelTypeMappings(List<TopLevelType> topLevelTypes) {
        this.topLevelTypes = topLevelTypes;
        this.providedTypes = new HashSet<>();
        this.requiredTypes = new HashSet<>();
        this.tlt2providedTopLevelTypes = new HashMap<>();
        this.tlt2requiredTopLevelTypeFields = new HashMap<>();
        this.topLevelType2MethodWithIF = new HashMap<>();
        this.dataSet2TopLevelTypes = new HashMap<>();
    }

    public List<TopLevelType> getTopLevelTypes() {
        return topLevelTypes;
    }

    public Set<TopLevelType> getRequiredTopLevelTypes() {
        return requiredTypes;
    }

    public Set<TopLevelType> getProvidedTopLevelTypes() {
        return providedTypes;
    }

    public Set<TopLevelType> unionOfProvidedAndRequiredTypes() {
        Set<TopLevelType> merge = new HashSet<>();
        merge.addAll(getProvidedTopLevelTypes());
        merge.addAll(getRequiredTopLevelTypes());
        return merge;
    }

    public boolean hasAnyIFAnnotation(TopLevelType topLevelType) {
        return tlt2providedTopLevelTypes.containsKey(topLevelType)
                || tlt2requiredTopLevelTypeFields.containsKey(topLevelType);
    }

    public boolean hasProvidedTopLevelTypes(TopLevelType topLevelType) {
        return tlt2providedTopLevelTypes.containsKey(topLevelType);
    }

    public boolean hasRequiredTopLevelTypes(TopLevelType topLevelType) {
        return tlt2requiredTopLevelTypeFields.containsKey(topLevelType);
    }

    public void addRequiredTopLevelTypeFields(TopLevelType type, List<TopLevelType.Field> toAdd) {
        toAdd.forEach(e -> requiredTypes.add(e.getTopLevelType()));
        List<TopLevelType.Field> list = new LinkedList<>();
        if (!tlt2requiredTopLevelTypeFields.containsKey(type)) {
            tlt2requiredTopLevelTypeFields.put(type, list);
        }
        list = tlt2requiredTopLevelTypeFields.get(type);
        list.addAll(toAdd);
    }

    public List<TopLevelType.Field> getRequiredTopLevelTypeFields(TopLevelType type) {
        return tlt2requiredTopLevelTypeFields.get(type);
    }

    public void addProvidedTopLevelTypes(TopLevelType type, List<TopLevelType> toAdd) {
        providedTypes.addAll(toAdd);
        List<TopLevelType> list = new LinkedList<>();

        if (!tlt2providedTopLevelTypes.containsKey(type)) {
            tlt2providedTopLevelTypes.put(type, list);
        }
        list = tlt2providedTopLevelTypes.get(type);
        list.addAll(toAdd);
    }

    public List<TopLevelType> getProvidedTopLevelTypes(TopLevelType type) {
        return tlt2providedTopLevelTypes.get(type);
    }

    public void addMethodWithIFToTopLevelType(TopLevelType type, IMethod method, InformationFlowAnnotation annotation) {
        List<Pair<IMethod, InformationFlowAnnotation>> list = new LinkedList<>();
        if (!topLevelType2MethodWithIF.containsKey(type)) {
            topLevelType2MethodWithIF.put(type, list);
        }
        list = topLevelType2MethodWithIF.get(type);
        list.add(new Pair<IMethod, InformationFlowAnnotation>(method, annotation));
    }

    public List<Pair<IMethod, InformationFlowAnnotation>> getMethodWithIF(TopLevelType type) {
        return topLevelType2MethodWithIF.get(type);
    }

    public void addTopLevelTypeAsSpecificationForDataSet(DataSet dataSet, TopLevelType type) {
        Set<TopLevelType> set = new HashSet<>();
        if (!dataSet2TopLevelTypes.containsKey(dataSet)) {
            dataSet2TopLevelTypes.put(dataSet, set);
        }
        set = dataSet2TopLevelTypes.get(dataSet);
        set.add(type);
    }

    public boolean hasTypeSpecificationForDataSet(TopLevelType type, DataSet dataSet) {
        return dataSet2TopLevelTypes.get(dataSet).contains(type);
    }
}
