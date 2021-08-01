package twitter.test;

import org.junit.jupiter.api.Test;
import twitter.PlaybackTwitterSource;
import twitter4j.Status;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the basic functionality of the TwitterSource
 */
public class TestPlaybackTwitterSource {

    @Test
    public void testSetup() {
        PlaybackTwitterSource source = new PlaybackTwitterSource(1.0);
        TestObserver testObserver = new TestObserver();
        // TODO: Once your TwitterSource class implements Observable, you must add the TestObserver as an observer to it here
        source.addObserver(testObserver);
        source.setFilterTerms(set("food"));
        pause(3 * 1000);
        assertTrue(testObserver.getNumberOfTweets() > 0, "Expected getNTweets() to be > 0, was " + testObserver.getNumberOfTweets());
        assertTrue(testObserver.getNumberOfTweets() <= 10, "Expected getNTweets() to be <= 10, was " + testObserver.getNumberOfTweets());
        int firstBunch = testObserver.getNumberOfTweets();
        System.out.println("Now adding 'the'");
        source.setFilterTerms(set("food", "the"));
        pause(3 * 1000);
        assertTrue(testObserver.getNumberOfTweets() > 0, "Expected getNTweets() to be > 0, was " + testObserver.getNumberOfTweets());
        assertTrue(testObserver.getNumberOfTweets() > firstBunch, "Expected getNTweets() to be < firstBunch (" + firstBunch + "), was " + testObserver.getNumberOfTweets());
        assertTrue(testObserver.getNumberOfTweets() <= 10, "Expected getNTweets() to be <= 10, was " + testObserver.getNumberOfTweets());
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private <E> Set<E> set(E ... set) {
        Set<E> terms = new HashSet<>();
        for (E element : set) {
            terms.add(element);
        }
        return terms;
    }
    private class TestObserver implements Observer {
        private int numberOfTweets = 0;

        @Override
        public void update(Observable observable, Object argument) {
            Status status = (Status) argument;
            numberOfTweets ++;
        }

        public int getNumberOfTweets() {
            return numberOfTweets;
        }
    }
}
