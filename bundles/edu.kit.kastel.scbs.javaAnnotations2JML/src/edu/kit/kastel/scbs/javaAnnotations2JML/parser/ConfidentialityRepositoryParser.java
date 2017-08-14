package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParametersAndDataPair;
import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.JdtAstJmlUtil;

/**
 * Parser for the confidentiality repository package.
 * 
 * TODO
 * 
 * @author Nils Wilka
 * @version 1.0, 02.08.2017
 */
public class ConfidentialityRepositoryParser
        extends JavaAnnotations2JMLParser<IJavaProject, ConfidentialitySpecification> {

    private static final String CONFIDENTIALITY_REPOSITORY = "confidentialityRepository";

    private static final String DATASETS = "DataSets";

    private static final String PARAMETERS_AND_DATA_PAIRS = "ParametersAndDataPairs";

    // 'InformationFlow' type is not required
    private static final String[] REQUIRED_CONFIDENTIALITY_TYPE_NAMES = { DATASETS, PARAMETERS_AND_DATA_PAIRS };

    private IPackageFragment confidentialityPackage;

    private ConfidentialitySpecification specification;

    private Map<String, IType> requiredConfidentialityTypes;

    public ConfidentialityRepositoryParser(IJavaProject source) {
        super(source);
        this.requiredConfidentialityTypes = new HashMap<>(REQUIRED_CONFIDENTIALITY_TYPE_NAMES.length);
        this.specification = new ConfidentialitySpecification();
    }

    @Override
    protected ConfidentialitySpecification parseSource() throws ParseException {
        try {
            setConfidentialityPackage();
            setJavaTypes();
            // TODO check if already present
            specification.addAllDataSets(parseDataSets());
            specification.addAllParameterAndDataPairs(parseParameterAndDataPairs());
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
        return specification;
    }

    private void setConfidentialityPackage() throws JavaModelException, ParseException {
        List<IPackageFragment> fragments = Arrays.asList(getSource().getPackageFragments());
        Optional<IPackageFragment> optional = fragments.stream()
                .filter(e -> e.getElementName().endsWith(CONFIDENTIALITY_REPOSITORY)).findFirst();
        this.confidentialityPackage = optional.orElseThrow(
                () -> new ParseException("No package with the name \"" + CONFIDENTIALITY_REPOSITORY + "\" found."));
    }

    private void setJavaTypes() throws ParseException, JavaModelException {
        for (String name : REQUIRED_CONFIDENTIALITY_TYPE_NAMES) {
            setJavaType(name);
        }
    }

    private void setJavaType(String name) throws ParseException, JavaModelException {
        IType javaType = getJavaTypeByName(name);
        checkEnum(javaType);
        requiredConfidentialityTypes.put(name, javaType);
    }

    private void checkEnum(IType type) throws ParseException, JavaModelException {
        if (!type.isEnum()) {
            throw new ParseException("java type " + type.getElementName() + " is not an enum type.");
        }
    }

    private IType getJavaTypeByName(String name) throws ParseException, JavaModelException {
        for (ICompilationUnit unit : confidentialityPackage.getCompilationUnits()) {
            List<IType> types = Arrays.asList(unit.getTypes());
            // more than one type permitted,
            // but the first one must be the expected one
            Optional<IType> type = types.stream().findFirst();
            if (type.isPresent() && type.get().getElementName().equals(name)) {
                return type.get();
            }
        }
        throw new ParseException("No java type with the name \"" + name + "\" found.");
    }

    private List<DataSet> parseDataSets() throws JavaModelException, ParseException {
        List<EnumConstantDeclaration> enumConstantDeclarations = parseEnumDeclarations(
                requiredConfidentialityTypes.get(DATASETS));
        List<DataSetArguments> arguments = new DataSetArgumentsParser(enumConstantDeclarations).parse();
        return arguments.stream().map(e -> new DataSet(e.getId(), e.getName(), e.getEnumConstant()))
                .collect(Collectors.toList());
    }

    private List<ParametersAndDataPair> parseParameterAndDataPairs() throws JavaModelException, ParseException {
        List<EnumConstantDeclaration> enumConstantDeclarations;
        enumConstantDeclarations = parseEnumDeclarations(requiredConfidentialityTypes.get(PARAMETERS_AND_DATA_PAIRS));
        List<ParametersAndDataPairArguments> argumentsList;
        argumentsList = new ParametersAndDataPairArgumentsParser(enumConstantDeclarations).parse();

        List<ParametersAndDataPair> parametersAndDataPairs = new LinkedList<>();
        for (ParametersAndDataPairArguments argument : argumentsList) {
            EnumConstant enumConstant = argument.getEnumConstant();
            List<ParameterSource> paramterSources = parseParameterSources(argument.getParameterSources());
            // throws ParseException:
            List<DataSet> dataSets = getDataSetsByName(argument.getDataSets());
            parametersAndDataPairs.add(new ParametersAndDataPair(enumConstant, paramterSources, dataSets));
        }
        return parametersAndDataPairs;
    }

    private List<ParameterSource> parseParameterSources(List<String> arguments) {
        return arguments.stream().map(ParameterSource::new).collect(Collectors.toList());
    }

    private List<DataSet> getDataSetsByName(List<EnumConstant> dataSetEnumConstants) throws ParseException {
        List<DataSet> dataSets = new LinkedList<>();
        for (EnumConstant argument : dataSetEnumConstants) {
            Optional<DataSet> optional = specification.getDataSetFromEnumConstantName(argument.getFullName());

            DataSet dataSet = optional
                    .orElseThrow(() -> new ParseException("Unexpected data set with the name " + argument));
            dataSets.add(dataSet);
        }
        return dataSets;
    }

    /**
     * Retrieves the {@code EnumConstantDeclaration}s from the given {@code IType}.
     * {@code EnumConstantDeclaration}s are the enum fields at the start of an enum type.
     * 
     * If this type is not an {@code EnumDeclaration}, the result will be an empty list.
     * 
     * @param type
     *            The type to retrieve the {@code EnumConstantDeclaration}s from.
     * @return A list of the enum fields.
     * @throws JavaModelException
     *             if the parsing of the {@code EnumConstantDeclaration} triggers them.
     */
    // the java doc of CompilationUnit#types() specifies the type 'AbstractTypeDeclaration'
    @SuppressWarnings("unchecked")
    private List<EnumConstantDeclaration> parseEnumDeclarations(IType type) throws JavaModelException {
        List<EnumConstantDeclaration> enumConstants = new LinkedList<>();

        CompilationUnit cu = JdtAstJmlUtil.setupParserAndGetCompilationUnit(type.getCompilationUnit());
        AbstractTypeDeclaration atd = JdtAstJmlUtil.findAbstractTypeDeclarationFromIType(cu.types(), type);
        if (atd instanceof EnumDeclaration) {
            EnumDeclaration ed = (EnumDeclaration) atd;
            ed.enumConstants().forEach(o -> enumConstants.add((EnumConstantDeclaration) o));
        }
        return enumConstants;
    }
}
