package edu.kit.kastel.scbs.javaAnnotations2JML.generator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParametersAndDataPair;
import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.EnumConstant;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.MethodAcceptor;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.SourceMethodProvider;
import edu.kit.kastel.scbs.javaAnnotations2JML.util.Anno2JmlUtil;

/**
 * Used to parse {@code IMethod}s and their {@code InformationFlowAnnotation}s with the help of a
 * {@code InformationFlowAnnotationParser} and in the context of the given
 * {@code ConfidentialitySpecification}. Gets the methods from the {@code MethodProvider} and saves
 * the result in the {@code MethodAcceptor}.
 * 
 * @author Nils Wilka
 * @version 1.0, 17.08.2017
 */
public class MethodsAndInformationFlowPairsGenerator {

    private ConfidentialitySpecification specification;

    private MethodAcceptor methodAcceptor;

    private SourceMethodProvider sourceProvider;

    /**
     * Creates a new java model parser for methods and their information flow annotations.
     * 
     * @param specification
     *            The context of the information flow annotations and their content.
     * @param methodAcceptor
     *            Where to put the resulting methods and their information flow annotations.
     * @param sourceMethodProvider
     *            The source of methods to parse.
     */
    public MethodsAndInformationFlowPairsGenerator(ConfidentialitySpecification specification,
            MethodAcceptor methodAcceptor, SourceMethodProvider sourceMethodProvider) {
        this.specification = specification;
        this.methodAcceptor = methodAcceptor;
        this.sourceProvider = sourceMethodProvider;
    }

    /**
     * Parses all provided methods and sets the results in the given acceptor.
     * 
     * @throws ParseException
     *             if the {@code InformationFlowAnnotationParser} for any method from the
     *             {@code SourceMethodProvider} throws it or if any reference to elements of the
     *             confidentiality specification are faulty, i.e. do reference an non-existing
     *             element.
     */
    public void parse() throws ParseException {
        try {
            parseMethodsAndAnnotations(sourceProvider, methodAcceptor);
        } catch (JavaModelException jme) {
            Optional<String> message = Optional.ofNullable(jme.getMessage());
            throw new ParseException("Java Model Exception occurred: " + message.orElse("(no error message)"), jme);
        }
    }

    /**
     * Gets all methods from the {@code SourceMethodProvider}, parses them (i.e. getting the
     * information flow annotation and resolving all reference to the confidentiality specification)
     * and adds the result to the {@code MethodAcceptor}.
     * 
     * In other words: Maps the given method to its information flow annotation.
     * 
     * Result does not contain method without an information flow annotation.
     * 
     * @param source
     *            The source to get the methods from.
     * @param acceptor
     *            To add the result to.
     * @throws JavaModelException
     *             if checking whether a method has an information flow annotation causes it.
     * @throws ParseException
     *             if the {@code InformationFlowAnnotationParser} throws it or the enum constants
     *             referencing parameters and data pairs from the confidentiality specification are
     *             faulty, i.e. do reference a missing parameters and data pair.
     */
    private void parseMethodsAndAnnotations(SourceMethodProvider source, MethodAcceptor acceptor)
            throws JavaModelException, ParseException {
        List<IMethod> methods = source.getSourceMethods();
        for (IMethod method : methods) {
            // only look at methods with information flow annotation
            if (Anno2JmlUtil.hasInformationFlowAnnotation(method)) {
                InformationFlowAnnotation annotation = parseInformationFlowAnnotation(specification, method);
                // service created from method and annotation
                acceptor.addMethod(method, annotation);
            }
        }
    }

    /**
     * Parses the {@code InformationFlowAnnotation} of the given {@code IMethod} in the context of
     * the given {@code ConfidentialitySpecification} with the help of an
     * {@code InformationFlowAnnotationParser}.
     * 
     * Multiple information flow annotations are seen as one annotation.
     * 
     * @param specification
     *            The context of all confidentiality objects.
     * @param method
     *            The method to get the information flow annotation for.
     * @return The information flow annotation for the given method.
     * @throws ParseException
     *             if the {@code InformationFlowAnnotationParser} throws it or the enum constants
     *             referencing parameters and data pairs from the confidentiality specification are
     *             faulty, i.e. do reference a missing parameters and data pair.
     */
    private InformationFlowAnnotation parseInformationFlowAnnotation(ConfidentialitySpecification specification,
            IMethod method) throws ParseException {
        Map<EnumConstant, Optional<ParametersAndDataPair>> enumConstantPairCorrespondences;
        enumConstantPairCorrespondences = parseAnnotationAndGetCorrespondingPairs(specification, method);
        List<ParametersAndDataPair> parametersAndDataPairs = retrieveCorrespondencesOrThrowException(method,
                enumConstantPairCorrespondences);
        return new InformationFlowAnnotation(parametersAndDataPairs);
    }

    /**
     * Scans all enum constants of the given map and checks if the image is an empty value. If so,
     * an exception is thrown, else it will be added to the returned list.
     * 
     * @param method
     *            The method with the information flow annotation the enum constants belong to.
     * @param enumConstantPairCorrespondences
     *            enum constants mapped to their referenced parameters and data pairs or an empty
     *            value.
     * @return The values of the given if there was not empty value.
     * @throws ParseException
     *             if there exists an enum constant that maps to an empty value.
     */
    private List<ParametersAndDataPair> retrieveCorrespondencesOrThrowException(IMethod method,
            Map<EnumConstant, Optional<ParametersAndDataPair>> enumConstantPairCorrespondences) throws ParseException {
        List<ParametersAndDataPair> parametersAndDataPairs = new LinkedList<>();
        for (EnumConstant pairEnumConstant : enumConstantPairCorrespondences.keySet()) {
            // throw exception if no fitting ParameterAndDataPair was found before
            // (i.e. optional is empty)
            ParametersAndDataPair pair = enumConstantPairCorrespondences.get(pairEnumConstant)
                    .orElseThrow(() -> new ParseException("Unexpected parameters and data pair " + pairEnumConstant
                            + " in the annotation of the method " + method.getElementName()));
            parametersAndDataPairs.add(pair);
        }
        return parametersAndDataPairs;
    }

    /**
     * Uses an {@code InformationFlowAnnotationParser} to get the {@code InformationFlowAnnotation}
     * from the given {@code IMethod}. Then gets the {@code ParametersAndDataPair}s referenced by
     * the annotation from the {@code ConfidentialitySpecification}.
     * 
     * As the references might be faulty, the specification may return empty values.
     * 
     * @param specification
     *            The context of the {@code ParametersAndDataPair}s from the information flow
     *            annotation.
     * @param method
     *            The method to get the {@code InformationFlowAnnotation} from.
     * @return A map of enum constants referencing {@code ParametersAndDataPair}s as optionals
     *         contained in the information flow annotation from the given method and ideally all
     *         present. Faulty reference result in empty values.
     * @throws ParseException
     *             if thrown by the {@code InformationFlowAnnotationParser}.
     */
    private Map<EnumConstant, Optional<ParametersAndDataPair>> parseAnnotationAndGetCorrespondingPairs(
            ConfidentialitySpecification specification, IMethod method) throws ParseException {
        InformationFlowAnnotationArguments arguments = new InformationFlowAnnotationGenerator(method).parse();
        Map<EnumConstant, Optional<ParametersAndDataPair>> pairMap = new HashMap<>();
        // Optional's are empty if there was no fitting ParameterAndDataPair
        arguments.getParametersAndDataPairEnumConstants().forEach(e -> pairMap.put(e,
                specification.getParametersAndDataPairFromEnumConstantName(e.getEnumConstantFullName())));
        return pairMap;
    }
}
