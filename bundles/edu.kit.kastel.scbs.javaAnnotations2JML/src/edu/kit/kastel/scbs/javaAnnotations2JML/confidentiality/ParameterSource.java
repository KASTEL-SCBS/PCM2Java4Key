package edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality;

import java.util.List;

/**
 * Represents a parameter source. A parameter source is declared in a {@code ParametersAndDataPair}.
 * It belongs to a service and will be added to a jml comment for a data set.
 * 
 * A parameter source can either contain information about the result of a service or it can be a
 * call parameter source or neither of both.
 * 
 * @author Nils Wilka
 * @version 1.1, 17.09.2017
 */
public class ParameterSource {

    private static final String CALL = "call";

    private static final String RESULT = "result";

    private final String name;

    /**
     * Creates a new parameter source with the given name.
     * 
     * @param name
     *            The name of the parameter source.
     */
    public ParameterSource(String name) {
        this.name = name;
    }

    /**
     * Checks whether this parameter source contains information about the result.
     * 
     * @return Whether this parameter source contains information about the result.
     */
    public boolean isResult() {
        return name.contains(RESULT);
    }

    /**
     * Checks whether this parameter source is a "call" parameter source.
     * 
     * @return Whether this parameter source is a "call" parameter source.
     */
    public boolean isCall() {
        return name.equals(CALL);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ParameterSource) {
            ParameterSource other = (ParameterSource) obj;
            return this.name.equals(other.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * String representation of a list with parameter sources.
     * 
     * @param paramterSources
     *            The list of parameter sources to get the string representation for.
     * @return A string representation of a list with parameter sources.
     */
    public static String toString(List<ParameterSource> paramterSources) {
        StringBuilder sb = new StringBuilder(paramterSources.get(0).toString());
        for (int i = 1; i < paramterSources.size(); i++) {
            sb.append(", ").append(paramterSources.get(i));
        }
        return sb.toString();
    }
}
