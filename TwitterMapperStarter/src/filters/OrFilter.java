package filters;

import twitter4j.Status;

import java.util.List;
import java.util.ArrayList;

/**
 * A filter that represents the logical OR of its children filters
 */
public class OrFilter implements Filter {
    private final Filter child1;
    private final Filter child2;

    public OrFilter(Filter child1, Filter child2) {
        this.child1 = child1;
        this.child2 = child2;
    }

    /**
     * An OR filter matches when only 1 child matches
     * @param s     the tweet to check
     * @return      whether or not it matches
     */
    @Override
    public boolean matches(Status s) {
        return child1.matches(s) || child2.matches(s);
    }

    @Override
    public List<String> terms() {
        List<String> terms = new ArrayList<>(2);
        terms.add(child1.terms().get(0));
        terms.add(child2.terms().get(0));
        return terms;
    }

    public String toString() {
        return "(" + child1.toString() + " or " + child2.toString() + ")";
    }
}