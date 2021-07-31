package filters;

/**
 * Parse a string in the filter language and return the filter.
 * Throws a SyntaxError exception on failure.
 *
 * This is a top-down recursive descent parser (a.k.a., LL(1))
 *
 * The really short explanation is that an LL(1) grammar can be parsed by a collection
 * of mutually recursive methods, each of which is able to recognize a single grammar symbol.
 *
 * The grammar (EBNF) for our filter language is:
 *
 * goal    ::= expr
 * expr    ::= orexpr
 * orexpr  ::= andexpr ( "or" andexpr )*
 * andexpr ::= notexpr ( "and" notexpr )*
 * notexpr ::= prim | "not" notexpr
 * prim    ::= word | "(" expr ")"
 *
 * The reason for writing it this way is that it respects the "natural" precedence of boolean
 * expressions, where the precedence order (decreasing) is:
 *      parens
 *      not
 *      and
 *      or
 * This allows an expression like:
 *      blue or green and not red or yellow and purple
 * To be parsed like:
 *      blue or (green and (not red)) or (yellow and purple)
 */
public class Parser {
    private final Scanner scanner;
    private static final String LPAREN = "(";
    private static final String RPAREN = ")";
    private static final String OR = "or";
    private static final String AND = "and";
    private static final String NOT = "not";

    public Parser(String input) {
        scanner = new Scanner(input);
    }

    public Filter parse() throws SyntaxError {
        Filter terms = expression();
        if (scanner.peek() != null) {
            throw new SyntaxError("Extra stuff at end of input");
        }
        return terms;
    }

    private Filter expression() throws SyntaxError {
        return orExpression();
    }

    private Filter orExpression() throws SyntaxError {
        Filter leftSubExpression = andExpression();
        String token = scanner.peek();
        while (token != null && token.equals(OR)) {
            scanner.advance();
            Filter rightSubExpression = andExpression();
            // At this point we have two subexpressions ("leftSubExpression" on the left and "rightSubExpression" on the right)
            // that are to be connected by "or"
            // TODO: Construct the appropriate new Filter object
            // The new filter object should be assigned to the variable "sub"
            leftSubExpression = new OrFilter(leftSubExpression, rightSubExpression);
            token = scanner.peek();
        }
        return leftSubExpression;
    }

    private Filter andExpression() throws SyntaxError {
        Filter leftSubExpression = notExpression();
        String token = scanner.peek();
        while (token != null && token.equals(AND)) {
            scanner.advance();
            Filter rightSubExpression = notExpression();
            // At this point we have two subexpressions ("leftSubExpression" on the left and "rightSubExpression" on the right)
            // that are to be connected by "and"
            // TODO: Construct the appropriate new Filter object
            // The new filter object should be assigned to the variable "sub"
            leftSubExpression = new AndFilter(leftSubExpression, rightSubExpression);
            token = scanner.peek();
        }
        return leftSubExpression;
    }

    private Filter notExpression() throws SyntaxError {
        String token = scanner.peek();
        if (token.equals(NOT)) {
            scanner.advance();
            Filter subExpression = notExpression();
            return new NotFilter(subExpression);
        } else {
            Filter subExpression = prim();
            return subExpression;
        }
    }

    private Filter prim() throws SyntaxError {
        String token = scanner.peek();
        if (token.equals(LPAREN)) {
            scanner.advance();
            Filter sub = expression();
            if (!scanner.peek().equals(RPAREN)) {
                throw new SyntaxError("Expected ')'");
            }
            scanner.advance();
            return sub;
        } else {
            Filter sub = new BasicFilter(token);
            scanner.advance();
            return sub;
        }
    }
}
