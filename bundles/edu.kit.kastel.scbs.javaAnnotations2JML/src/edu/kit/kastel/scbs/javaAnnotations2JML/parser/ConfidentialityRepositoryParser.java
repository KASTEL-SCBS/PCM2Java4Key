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
 * Java model parser for the confidentiality repository package. Looks for the confidentiality
 * package. Parses included data sets and parameters and data pair java classes. Sets all present
 * data sets and parameters and data pairs.
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

    /**
     * Creates a new java model parser for the confidentiality package.
     * 
     * @param source
     *            The java project to parse.
     */
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
            specification.addAllDataSets(parseDataSets());
            specification.addAllParameterAndDataPairs(parseParameterAndDataPairs());
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
        return specification;
    }

    /**
     * Searches for the confidentiality package in the given java project and sets it.
     * 
     * @throws JavaModelException
     *             if scanning the package fragments causes it.
     * @throws ParseException
     *             if the confidentiality package is missing.
     */
    private void setConfidentialityPackage() throws JavaModelException, ParseException {
        List<IPackageFragment> fragments = Arrays.asList(getSource().getPackageFragments());
        Optional<IPackageFragment> optional = fragments.stream()
                .filter(e -> e.getElementName().endsWith(CONFIDENTIALITY_REPOSITORY)).findFirst();
        this.confidentialityPackage = optional.orElseThrow(
                () -> new ParseException("No package with the name \"" + CONFIDENTIALITY_REPOSITORY + "\" found."));
    }

    /**
     * Searches for all required confidentiality enums name in the confidentiality package and makes
     * them accessible by name. Throws an {@code ParseException} if one file is not an enum.
     * 
     * @throws ParseException
     *             if one of the java files is not of type enum if there is a java file missing.
     * @throws JavaModelException
     *             if scanning the source files causes it.
     */
    private void setJavaTypes() throws ParseException, JavaModelException {
        for (String name : REQUIRED_CONFIDENTIALITY_TYPE_NAMES) {
            setJavaType(name);
        }
    }

    /**
     * Searches for given required confidentiality enum name in the confidentiality package and
     * makes it accessible by name. Throws an {@code ParseException} if the file is not an enum.
     * 
     * @param name
     *            The name of the java enum to find.
     * @throws ParseException
     *             if the java file is not of type enum or if there is no java file with the given
     *             name.
     * @throws JavaModelException
     *             if scanning the source files causes it.
     */
    private void setJavaType(String name) throws ParseException, JavaModelException {
        IType javaType = getJavaTypeByName(name);
        checkEnum(javaType);
        requiredConfidentialityTypes.put(name, javaType);
    }

    /**
     * Checks if the given {@code IType} is an enum and throws an exception if not.
     * 
     * @param type
     *            THe type to check.
     * @throws ParseException
     *             if the java file is not of type enum or if there is no java file with the given
     *             name.
     * @throws JavaModelException
     *             if checking for enum causes it.
     */
    private void checkEnum(IType type) throws ParseException, JavaModelException {
        if (!type.isEnum()) {
            throw new ParseException("java type " + type.getElementName() + " is not an enum type.");
        }
    }

    /**
     * Searches for given required confidentiality enum name in the confidentiality package and
     * returns it.
     * 
     * @param name
     *            The name of the java enum to find.
     * @return The enum with the given name.
     * @throws ParseException
     *             if there is no java file with the given name.
     * @throws JavaModelException
     *             if scanning the source files causes it.
     */
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

    /**
     * Parses all enum constant declarations in the data set enum file of the confidentiality
     * package and calls the {@code DataSetArgumentsParser}.
     * 
     * @return The {@code DataSet}s in the confidentiality package.
     * @throws JavaModelException
     *             if the parsing of the {@code EnumConstantDeclaration} triggers them.
     * @throws ParseException
     *             if the {@code DataSetArgumentsParser} throws it.
     */
    private List<DataSet> parseDataSets() throws JavaModelException, ParseException {
        List<EnumConstantDeclaration> enumConstantDeclarations = parseEnumDeclarations(
                requiredConfidentialityTypes.get(DATASETS));
        List<DataSetArguments> arguments = new DataSetArgumentsParser(enumConstantDeclarations).parse();
        return arguments.stream().map(e -> new DataSet(e.getId(), e.getName(), e.getEnumConstant()))
                .collect(Collectors.toList());
    }

    /**
     * Parses all enum constant declarations in the parameters and data pairs enum file of the
     * confidentiality package and calls the {@code ParametersAndDataPairArgumentsParser}.
     * 
     * This includes the parameter sources and the data sets. The latter are retrieved from the
     * confidentiality specification by their enum constant.
     * 
     * @return The {@code ParametersAndDataPair}s in the confidentiality package.
     * @throws JavaModelException
     *             if the parsing of the {@code EnumConstantDeclaration} triggers them.
     * @throws ParseException
     *             if the {@code ParametersAndDataPairArgumentsParser} throws it.
     */
    private List<ParametersAndDataPair> parseParameterAndDataPairs() throws JavaModelException, ParseException {
        List<EnumConstantDeclaration> enumConstantDeclarations;
        enumConstantDeclarations = parseEnumDeclarations(requiredConfidentialityTypes.get(PARAMETERS_AND_DATA_PAIRS));
        List<ParametersAndDataPairArguments> argumentsList;
        argumentsList = new ParametersAndDataPairArgumentsParser(enumConstantDeclarations).parse();

        List<ParametersAndDataPair> parametersAndDataPairs = new LinkedList<>();
        for (ParametersAndDataPairArguments argument : argumentsList) {
            EnumConstant enumConstant = argument.getEnumConstant();
            List<ParameterSource> paramterSources = parseParameterSources(argument.getParameterSourceStrings());
            // throws ParseException:
            List<DataSet> dataSets = getDataSetsByName(argument.getDataSetEnumConstants());
            parametersAndDataPairs.add(new ParametersAndDataPair(enumConstant, paramterSources, dataSets));
        }
        return parametersAndDataPairs;
    }

    /**
     * Creates the parameter sources from the given string list.
     * 
     * @param arguments
     *            The parameter sources as strings.
     * @return The parameter sources of type {@code ParameterSource}.
     */
    private List<ParameterSource> parseParameterSources(List<String> arguments) {
        return arguments.stream().map(ParameterSource::new).collect(Collectors.toList());
    }

    /**
     * Retrieves data sets by their corresponding enum constants enum constants from the
     * confidentiality specification.
     * 
     * @param dataSetEnumConstants
     *            The enum constants representing data sets in the confidentiality specification.
     * @return The data sets from the confidentiality specification corresponding to the given list
     *         of enum constants.
     * @throws ParseException
     *             if one enum constant cannot be resolved to a data set.
     */
    private List<DataSet> getDataSetsByName(List<EnumConstant> dataSetEnumConstants) throws ParseException {
        List<DataSet> dataSets = new LinkedList<>();
        for (EnumConstant argument : dataSetEnumConstants) {
            Optional<DataSet> optional = specification
                    .getDataSetFromEnumConstantName(argument.getEnumConstantFullName());

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
