package Errors;

public class ParsingTableConflictError extends RuntimeException {

    public ParsingTableConflictError(String symbol1, String symbol2, String value1, String value2) {
        super("Conflict in parsing table for pair (" + symbol1 + ", " + symbol2 +
                ") - values: (" + value1 + ") and (" + value2 + ")");
    }
}
