package filters;

import twitter4j.Status;

import java.util.List;
import java.util.ArrayList;

/**
 * A filter that represents the logical AND of its children filters
 */
public class AndFilter implements Filter {
    private final Filter leftChild;
    private final Filter rightChild;

    public AndFilter(Filter leftChild, Filter rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * An AND filter matches when both children match
     * @param status     the tweet to check
     * @return      whether or not it matches
     */
    @Override
    public boolean matches(Status status) {
        return leftChild.matches(status) && rightChild.matches(status);
    }

    @Override
    public List<String> terms() {
        List<String> terms = new ArrayList<>(2);
        terms.add(leftChild.terms().get(0));
        terms.add(rightChild.terms().get(0));
        return terms;
    }

    public String toString() {
        return "(" + leftChild.toString() + " and " + rightChild.toString() + ")";
    }
}