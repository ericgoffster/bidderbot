package bbidder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

public class Test2Over1 {
    public static BiddingSystem bs;

    @Test
    public void test() throws Throwable {
        AtomicBoolean hadError = new AtomicBoolean(false);
        BiddingSystem bs = BiddingSystem.load("classpath:bbo21/index.bidding", ex -> {
            hadError.set(true);
            ex.printStackTrace();
        });
        assertFalse(hadError.get());
        Random r = new Random();
        for (BiddingTest test : bs.tests) {
            TestResult result;
            try {
                result = test.getResult(r, bs);
            } catch (Exception e) {
                hadError.set(true);
                System.err.println("At " + test.where);
                System.err.println("Could not generate a hand");
                continue;
            }
            if (!result.found.bid.equals(result.expected)) {
                hadError.set(true);
                System.err.println("Test at " + result.where);
                System.err.println("says I should have bid "+result.expected);
                System.err.println("But "+result.found.where);
                System.err.println("Dictates "+result.found.bid);
                System.err.println();
                System.err.println("All bids matching the scenario");
                for(var b: result.found.possible) {
                    System.err.println("   "+b);
                    System.err.println("       "+b.bind(result.state.getLikelyHands()));
                }
                test.getResult(r, bs);
            }
        }
        System.out.println("tests = " + bs.tests.size());
        assertTrue(bs.tests.size() > 0);
        assertFalse(hadError.get());
    }
}
