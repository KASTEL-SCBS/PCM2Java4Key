package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.Anno2JmlUtil;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelType;
import edu.kit.kastel.scbs.javaAnnotations2JML.TopLevelTypeRelations;

public class RelatedTypesParser extends JavaAnnotations2JMLParser<List<TopLevelType>, TopLevelTypeRelations> {

    public RelatedTypesParser(List<TopLevelType> source) {
        super(source);
    }

    @Override
    public TopLevelTypeRelations parse() {
        TopLevelTypeRelations tltRelations = new TopLevelTypeRelations(getSource());

        for (TopLevelType type : getSource()) {
            List<TopLevelType.Field> requiredTopLevelTypeFields = null; // TODO
            List<TopLevelType> providedTopLevelTypes = null; // TODO
            try {
                requiredTopLevelTypeFields = getRequiredTopLevelTypeFields(type);
                providedTopLevelTypes = getProvidedTopLevelTypes(type);
            } catch (JavaModelException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!providedTopLevelTypes.isEmpty()) {
                tltRelations.addProvidedTopLevelTypeFields(type, providedTopLevelTypes);
            }
            if (!requiredTopLevelTypeFields.isEmpty()) {
                tltRelations.addRequiredTopLevelTypeFields(type, requiredTopLevelTypeFields);
            }
        }
        setResult(tltRelations);
        return getResult();
    }

    private List<TopLevelType.Field> getRequiredTopLevelTypeFields(final TopLevelType type) throws JavaModelException {
        List<TopLevelType.Field> fieldTypes = type.getAllTopLevelTypeFields();
        List<TopLevelType.Field> fieldsWithIFProperty = new LinkedList<>();
        for (TopLevelType.Field field : fieldTypes) {
            if (Anno2JmlUtil.hasInformationFlowAnnotation(field.getTopLevelType().getIType())) {
                // if its an top level type and has information flow annotations
                // then it is required.
                fieldsWithIFProperty.add(field);
            }
        }
        return fieldsWithIFProperty;
    }

    private List<TopLevelType> getProvidedTopLevelTypes(final TopLevelType type) throws JavaModelException {
        List<IType> implementedInterfaces = type.getSuperTypeInterfacesWithIFProperty();
        return TopLevelType.create(implementedInterfaces);
    }
}
