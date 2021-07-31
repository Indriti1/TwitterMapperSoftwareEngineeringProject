package filters;

/**
 * The exception thrown when parsing a string fails.
 */
public class SyntaxError extends Exception {
    public SyntaxError(String string) {
        super(string);
    }
    public SyntaxError() {
        super();
    }
}
