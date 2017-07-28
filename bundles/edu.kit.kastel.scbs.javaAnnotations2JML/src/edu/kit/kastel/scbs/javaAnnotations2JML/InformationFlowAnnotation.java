package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class InformationFlowAnnotation {

    private DataSet dataSet;

    private List<Argument> arguments;

    public InformationFlowAnnotation(DataSet dataSet, List<Argument> arguments) {
        this.dataSet = dataSet;
        this.arguments = arguments;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    // used to check whether there is a result argument
    public Optional<Argument> getResult() {
        Optional<Argument> result = Optional.empty();

        for (Argument argument : arguments) {
            if (argument.isResultArgument()) {
                result = Optional.of(argument);
                break;
            }
        }
        return result;
    }

    public List<Argument> getNonResultArguments() {
        List<Argument> nonResultArguments = new LinkedList<>();

        for (Argument argument : arguments) {
            if (!argument.isResultArgument()) {
                nonResultArguments.add(argument);
            }
        }
        return nonResultArguments;
    }

    @Override
    public String toString() {
        return "ANNOTATION( dataSet = '" + dataSet + "', arguments = '" + Argument.toString(arguments) + "')";
    }

    public static class Argument {

        private String name;

        public Argument(String name) {
            this.name = name;
        }

        public static List<Argument> create(String argumentsString) {
            String[] split = argumentsString.split(", ");
            List<Argument> arguments = new LinkedList<>();
            for (String name : split) {
                arguments.add(new Argument(name));
            }
            return arguments;
        }

        private boolean isResultArgument() {
            return name.equals("\\result");
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof Argument) {
                Argument other = (Argument) obj;
                return this.name.equals(other.name);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }

        // TODO move to JMLComment
        public static String toString(List<Argument> arguments) {
            StringBuilder sb = new StringBuilder(arguments.get(0).toString());
            for (int i = 1; i < arguments.size(); i++) {
                sb.append(", ").append(arguments.get(i));
            }
            return sb.toString();
        }
    }
}
