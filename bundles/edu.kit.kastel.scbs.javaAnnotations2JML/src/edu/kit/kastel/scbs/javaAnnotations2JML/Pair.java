package edu.kit.kastel.scbs.javaAnnotations2JML;

public class Pair<T, Z> {
    private T first;
    private Z second;

    public Pair(T a, Z b) {
        this.first = a;
        this.second = b;
    }

    public T getFirst() {
        return this.first;
    }

    public Z getSecond() {
        return this.second;
    }

    @Override
    public String toString() {
        return "PAIR(" + first.toString() + ", " + second.toString() + ")";
    }
}
