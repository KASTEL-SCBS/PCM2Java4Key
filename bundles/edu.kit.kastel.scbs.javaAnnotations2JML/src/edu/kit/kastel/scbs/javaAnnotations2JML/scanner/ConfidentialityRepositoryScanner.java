package edu.kit.kastel.scbs.javaAnnotations2JML.scanner;

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
import edu.kit.kastel.scbs.javaAnnotations2JML.generator.DataSetArguments;
import edu.kit.kastel.scbs.javaAnnotations2JML.generator.DataSetArgumentsGenerator;
import edu.kit.kastel.scbs.javaAnnotations2JML.generator.ParametersAndDataPairArguments;
import edu.kit.kastel.scbs.javaAnnotations2JML.generator.ParametersAndDataPairArgumentsGenerator;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.JdtAstJmlUtil;

/**
 * Scanner for the confidentiality repository package. Looks for the confidentiality package and
 * scans included data sets and parameters and data pair java classes. Generates all present data
 * sets and parameters and data pairs.
 * 
 * @author Nils Wilka
 * @version 1.1, 16.09.2017
 */
public class ConfidentialityRepositoryScanner {

    private static final String CONFIDENTIALITY_REPOSITORY = "confidentialityRepository";

    private static final String DATASETS = "DataSets";

    private static final String PARAMETERS_AND_DATA_PAIRS = "ParametersAndDataPairs";

    // 'InformationFlow' type is not required
    private static final String[] REQUIRED_CONFIDENTIALITY_TYPE_NAMES = { DATASETS, PARAMETERS_AND_DATA_PAIRS };

    private final ConfidentialitySpecification specification;

    private final Map<String, IType> requiredConfidentialityTypes;

    private final IJavaProject project;

    private IPackageFragment confidentialityPackage;

    /**
     * Creates a new scanner for the confidentiality repository package.
     * 
     * @param source
     *            The java project to scan and to create the confidentiality types for.
     */
    public ConfidentialityRepositoryScanner(IJavaProject source) {
        this.project = source;
        this.requiredConfidentialityTypes = new HashMap<>(REQUIRED_CONFIDENTIALITY_TYPE_NAMES.length);
        this.specification = new ConfidentialitySpecification();
    }

    /**
     * Scans the confidentiality repository package, i.e. scans included data sets and parameters
     * and data pair java classes. Generates all present data sets and parameters and data pairs.
     * 
     * @return The confidentiality specification created from the confidentiality repository
     *         package.
     * @throws ParseException
     *             if scanning the package fragments or source files causes it or if the scanning of
     *             the data sets or parameters and data pairs causes it.
     */
    public ConfidentialitySpecification scan() throws ParseException {
        try {
            setConfidentialityPackage();
            setJavaTypes();
            specification.addAllDataSets(scanDataSets());
            specification.addAllParameterAndDataPairs(scanParameterAndDataPairs());
        } catch (JavaModelException jme) {
            final Optional<String> message = Optional.ofNullable(jme.getMessage());
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
     *             if the confidentiality repository package is missing.
     */
    private void setConfidentialityPackage() throws JavaModelException, ParseException {
        final List<IPackageFragment> fragments = Arrays.asList(project.getPackageFragments());
        final Optional<IPackageFragment> optional = fragments.stream()
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
            final IType javaType = getJavaTypeByName(name);
            checkEnum(javaType);
            requiredConfidentialityTypes.put(name, javaType);
        }
    }

    /**
     * Checks if the given {@code IType} is an enum and throws an exception if not.
     * 
     * @param type
     *            The type to check.
     * @throws ParseException
     *             if the java file is not of type enum or if there is no java file with the given
     *             name.
     * @throws JavaModelException
     *             if checking for enum causes it.
     */
    private void checkEnum(final IType type) throws ParseException, JavaModelException {
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
    private IType getJavaTypeByName(final String name) throws ParseException, JavaModelException {
        for (ICompilationUnit unit : confidentialityPackage.getCompilationUnits()) {
            final List<IType> types = Arrays.asList(unit.getTypes());
            // more than one type permitted,
            // but the first one must be the expected one
            final Optional<IType> type = types.stream().findFirst();
            if (type.isPresent() && type.get().getElementName().equals(name)) {
                return type.get();
            }
        }
        throw new ParseException("No java type with the name \"" + name + "\" found.");
    }

    /**
     * Scans all enum constant declarations in the data set enum file of the confidentiality
     * repository package and calls the {@code DataSetArgumentsGenerator}.
     * 
     * @return The {@code DataSet}s in the confidentiality package.
     * @throws JavaModelException
     *             if the scanning of the {@code EnumConstantDeclaration} triggers it.
     * @throws ParseException
     *             if the {@code DataSetArgumentsGenerator} throws it.
     */
    private List<DataSet> scanDataSets() throws JavaModelException, ParseException {
        final List<EnumConstantDeclaration> enumConstantDeclarations = scanEnumDeclarations(
                requiredConfidentialityTypes.get(DATASETS));
        final List<DataSetArguments> arguments = new DataSetArgumentsGenerator(enumConstantDeclarations).generate();
        return arguments.stream().map(e -> new DataSet(e.getId(), e.getName(), e.getEnumConstant()))
                .collect(Collectors.toList());
    }

    /**
     * Parses all enum constant declarations in the parameters and data pairs enum file of the
     * confidentiality repository package and calls the
     * {@code ParametersAndDataPairArgumentsGenerator}.
     * 
     * This includes the parameter sources and the data sets. The latter are retrieved from the
     * confidentiality specification by their enum constant.
     * 
     * @return The {@code ParametersAndDataPair}s in the confidentiality package.
     * @throws JavaModelException
     *             if the scanning of the {@code EnumConstantDeclaration} triggers it.
     * @throws ParseException
     *             if the {@code ParametersAndDataPairArgumentsGenerator} throws it.
     */
    private List<ParametersAndDataPair> scanParameterAndDataPairs() throws JavaModelException, ParseException {
        final List<EnumConstantDeclaration> enumConstantDeclarations;
        enumConstantDeclarations = scanEnumDeclarations(requiredConfidentialityTypes.get(PARAMETERS_AND_DATA_PAIRS));
        final List<ParametersAndDataPairArguments> argumentsList;
        argumentsList = new ParametersAndDataPairArgumentsGenerator(enumConstantDeclarations).generate();

        final List<ParametersAndDataPair> parametersAndDataPairs = new LinkedList<>();
        for (ParametersAndDataPairArguments argument : argumentsList) {
            final EnumConstant enumConstant = argument.getEnumConstant();
            final List<ParameterSource> paramterSources = scanParameterSources(argument.getParameterSourceStrings());
            final List<DataSet> dataSets = getDataSetsByName(argument.getDataSetEnumConstants());
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
    private List<ParameterSource> scanParameterSources(final List<String> arguments) {
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
    private List<DataSet> getDataSetsByName(final List<EnumConstant> dataSetEnumConstants) throws ParseException {
        final List<DataSet> dataSets = new LinkedList<>();
        for (EnumConstant argument : dataSetEnumConstants) {
            final Optional<DataSet> optional = specification
                    .getDataSetFromEnumConstantName(argument.getEnumConstantFullName());

            final DataSet dataSet = optional
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
     *             if the scanning of the {@code EnumConstantDeclaration} triggers them.
     */
    // the java doc of CompilationUnit#types() specifies the type 'AbstractTypeDeclaration'
    @SuppressWarnings("unchecked")
    private List<EnumConstantDeclaration> scanEnumDeclarations(final IType type) throws JavaModelException {
        final List<EnumConstantDeclaration> enumConstants = new LinkedList<>();

        final CompilationUnit cu = JdtAstJmlUtil.setupParserAndGetCompilationUnit(type.getCompilationUnit());
        final AbstractTypeDeclaration atd = JdtAstJmlUtil.findAbstractTypeDeclarationFromIType(cu.types(), type);
        if (atd instanceof EnumDeclaration) {
            final EnumDeclaration ed = (EnumDeclaration) atd;
            ed.enumConstants().forEach(o -> enumConstants.add((EnumConstantDeclaration) o));
        }
        return enumConstants;
    }
}
