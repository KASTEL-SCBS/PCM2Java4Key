package edu.kit.kastel.scbs.javaAnnotations2JML.parser;

public abstract class JavaAnnotations2JMLParser<S, R> {

    private S source;

    private R result;

    public JavaAnnotations2JMLParser(S source) {
        this.source = source;
    }

    public abstract R parse();

    public S getSource() {
        return this.source;
    }

    public R getResult() {
        return this.result;
    }

    protected void setResult(R result) {
        this.result = result;
    }
}
