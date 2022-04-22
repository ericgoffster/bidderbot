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
        for (BiddingTest test : bs.getTests()) {
            TestResult result;
            try {
                result = test.getResult(r, bs);
            } catch (Exception e) {
                hadError.set(true);
                System.err.println("At " + test.where);
                System.err.println("Could not generate a hand");
                e.printStackTrace();
                continue;
            }
            if (!result.found.possibleBid.bid.equals(result.expected)) {
                hadError.set(true);
                InfSummary partnerSummary = result.state.players.partner.infSummary;
                System.err
                        .println("You have " + test.hand + ": " + test.hand.numHCP() + " hcp: " + test.hand.getTotalPoints(partnerSummary) + " tpts");
                System.err.println("My summary " + result.state.players.me.infSummary);
                System.err.println("Partner summary " + partnerSummary);
                System.err.println("Test at " + result.where + " claims I should have bid " + result.expected);
                if (result.found.possibleBid.inf != null) {
                    System.err.println("But " + result.found.possibleBid.inf.where + " dictates I should bid " + result.found.possibleBid.bid);
                } else {
                    System.err.println("But no systemic bid matched so " + result.found.possibleBid.bid + " was chosen");
                }
                System.err.println();
                System.err.println("All bids matching the scenario in order of priority:");
                for (PossibleBid b : result.found.getPossible()) {
                    if (b == result.found.possibleBid) {
                        System.err.println("   * " + b);
                    } else {
                        System.err.println("   " + b);
                    }
                    IBoundInference bi = b.inf.inferences.bind(result.state.players);
                    if (b == result.found.possibleBid) {
                        System.err.println("       * " + bi);

                    } else {
                        System.err.println("       " + bi);
                    }
                }
                test.getResult(r, bs);
            }
        }
        System.out.println("tests = " + bs.getTests().size());
        assertTrue(bs.getTests().size() > 0);
        assertFalse(hadError.get());
    }
}
