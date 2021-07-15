package filters.test;

import filters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the parser.
 */
public class TestParser {
    @Test
    public void testBasic() throws SyntaxError {
        Filter f = new Parser("trump").parse();
        assertTrue(f instanceof BasicFilter);
        assertTrue(((BasicFilter)f).getWord().equals("trump"));
    }

    @Test
    public void testNot() throws SyntaxError {
        Filter f = new Parser("not purple").parse();
        assertTrue(f instanceof NotFilter);
        assertTrue(((NotFilter)f).toString().equals("not purple"));
    }
    @Test
    public void testAnd() throws SyntaxError {
        Filter f = new Parser("green and purple").parse();
        assertTrue(f instanceof AndFilter);
        assertTrue(((AndFilter)f).toString().equals("(green and purple)"));
    }
    @Test
    public void testOr() throws SyntaxError {
        Filter f = new Parser("evil or blue").parse();
        assertTrue(f instanceof OrFilter);
        assertTrue(((OrFilter)f).toString().equals("(evil or blue)"));
    }

    @Test
    public void testHairy() throws SyntaxError {
        Filter x = new Parser("trump and (evil or blue) and red or green and not not purple").parse();
        assertTrue(x.toString().equals("(((trump and (evil or blue)) and red) or (green and not not purple))"));
    }
}
