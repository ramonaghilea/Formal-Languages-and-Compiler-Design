package utils;

public class Pair<E1, E2> {
    private final E1 first;
    private final E2 second;

    public Pair(E1 first, E2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return first +  "  " + second;
    }
}
