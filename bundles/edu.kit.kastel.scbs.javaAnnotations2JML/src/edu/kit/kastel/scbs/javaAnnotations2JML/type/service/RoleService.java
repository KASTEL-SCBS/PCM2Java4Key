package edu.kit.kastel.scbs.javaAnnotations2JML.type.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ParameterSource;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.JmlComment;

/**
 * An abstract service extends a service with a role, i.e. the context the service is used in.
 * 
 * @author Nils Wilka
 * @version 1.1, 29.09.2017
 */
public abstract class RoleService extends Service {

    private String role;

    /**
     * Creates a new abstract service with the given role, name and parameter sources.
     * 
     * @param role
     *            The role of the service.
     * @param name
     *            The name of the service.
     * @param parameterSources
     *            The parameter sources of the service.
     */
    public RoleService(String role, String name, List<ParameterSource> parameterSources) {
        super(name, parameterSources);
        this.role = role;
    }

    /**
     * Gets the role of this service type.
     * 
     * @return The role of this service type.
     */
    public String getRole() {
        return role;
    }

    /**
     * Adds the service to the given jml comment, i.e. the parameter sources. Differentiates between
     * "result", "call" and remaining parameter sources. Sub types decide how to add which parameter
     * source category.
     * 
     * @param comment
     *            The jml comment to add the service, i.e. its parameter sources to.
     */
    public void addServiceToJmlComment(JmlComment comment) {
        // four local variables for understandability and readability:
        List<ParameterSource> resultParams = new LinkedList<>();
        List<ParameterSource> nonResultParams = new LinkedList<>();
        List<ParameterSource> callParams = new LinkedList<>();
        List<ParameterSource> remainingParams = new LinkedList<>();
        partition(e -> e.isResult(), getParameterSources(), resultParams, nonResultParams);
        partition(e -> e.isCall(), nonResultParams, callParams, remainingParams);

        addResultLine(comment, resultParams);
        addNonResultLine(comment, remainingParams);
        if (!callParams.isEmpty()) {
            addVisibleLine(comment);
        }
    }

    /**
     * Partitions the {@code input} elements according to a given {@code predicate}.
     * 
     * Each element for which the predicate holds true, this element will be added to the
     * {@code trueList}.
     * 
     * Every other element will be added to the {@code falseList}.
     * 
     * @param predicate
     *            The predicate to partition the input elements with.
     * @param input
     *            The elements to apply the predicate to.
     * @param trueList
     *            Each element for which the predicate applies.
     * @param falseList
     *            Each element for which the predicate does not apply.
     */
    private void partition(final Predicate<ParameterSource> predicate, final List<ParameterSource> input,
            List<ParameterSource> trueList, List<ParameterSource> falseList) {
        Map<Boolean, List<ParameterSource>> partitionByPredicate;
        partitionByPredicate = input.stream().collect(Collectors.partitioningBy(predicate));
        trueList.addAll(partitionByPredicate.get(Boolean.TRUE));
        falseList.addAll(partitionByPredicate.get(Boolean.FALSE));
    }

    /**
     * Adds "non-result" parameter sources to the given jml comment.
     * 
     * @param comment
     *            The jml comment to add the parameter sources to.
     * @param nonResultParameterSources
     *            The parameter sources to add as "non-result".
     */
    protected abstract void addNonResultLine(JmlComment comment, List<ParameterSource> nonResultParameterSources);

    /**
     * Adds "result" parameter sources to the given jml comment.
     * 
     * @param comment
     *            The jml comment to add the parameter sources to.
     * @param resultParameterSources
     *            The parameter sources to add as "result".
     */
    protected abstract void addResultLine(JmlComment comment, List<ParameterSource> resultParameterSources);

    /**
     * Adds the call parameter source as visible to the given jml comment.
     * 
     * @param comment
     *            The jml comment to add the call parameter source as visible.
     */
    protected abstract void addVisibleLine(JmlComment comment);

    @Override
    public String toString() {
        return role + "." + super.toString();
    }
}
