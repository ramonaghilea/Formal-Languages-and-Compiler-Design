package Errors;

public class LexicalError extends RuntimeException {

    public LexicalError(Integer lineNumber, String description) {
        super("Lexical error at line " + lineNumber + ": " + description);
    }
}
