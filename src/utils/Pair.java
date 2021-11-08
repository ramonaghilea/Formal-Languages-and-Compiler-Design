package utils;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair<?, ?> pair)) return false;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    public E1 getFirst() {
        return first;
    }

    public E2 getSecond() {
        return second;
    }
}
