package bbidder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

public class Test2Over1 {
    public static BiddingSystem bs;
    
    public void showSummary(String label, InfSummary summary) {
        System.err.println(label);
        System.err.println("   " + summary.shape);
        System.err.println("   total points: " + summary.tpts);
        System.err.println("   stoppers: " + summary.stoppers);
        System.err.println("   partial: " + summary.partialStoppers);
    }

    @Test
    public void test() throws Throwable {
        AtomicBoolean hadError = new AtomicBoolean(false);
        BiddingSystem bs = BiddingSystemParser.load("classpath:bbo21/index.bidding", ex -> {
            hadError.set(true);
            ex.printStackTrace();
        });
        assertFalse(hadError.get());
        for (BiddingTest test : bs.getTests()) {
            TestResult result;
            try {
                result = test.getResult(bs);
            } catch (Exception e) {
                hadError.set(true);
                System.err.println("At " + test.where);
                System.err.println("Could not generate a hand");
                e.printStackTrace();
                continue;
            }
            if (result.ex != null) {
                hadError.set(true);
                InfSummary partnerSummary = result.state.players.partner.infSummary;
                System.err.println("Bidding: " + result.bids);
                System.err
                        .println("You have " + test.hand + ": " + test.hand.numHCP() + " hcp: " + test.hand.getTotalPoints(partnerSummary) + " tpts");
                showSummary("My summary", result.state.players.me.infSummary);
                showSummary("Partner summary", partnerSummary);
                System.err.println("Test at " + result.where + " claims I should have bid " + result.expected);
                System.err.println("But I got an exception: ");
                result.ex.printStackTrace();
                test.getResult(bs);
            } else if (!test.anti && !result.found.possibleBid.bid.equals(result.expected)) {
                hadError.set(true);
                InfSummary partnerSummary = result.state.players.partner.infSummary;
                System.err.println("Bidding: " + result.bids);
                System.err
                        .println("You have " + test.hand + ": " + test.hand.numHCP() + " hcp: " + test.hand.getTotalPoints(partnerSummary) + " tpts");
                showSummary("My summary", result.state.players.me.infSummary);
                showSummary("Partner summary", partnerSummary);
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
                //DebugUtils.debugMode = true;
                //DebugUtils.breakpoint();
                test.getResult(bs);
                //DebugUtils.debugMode = false;
            } else if (test.anti && result.found.possibleBid.bid.equals(result.expected)) {
                hadError.set(true);
                InfSummary partnerSummary = result.state.players.partner.infSummary;
                System.err
                        .println("You have " + test.hand + ": " + test.hand.numHCP() + " hcp: " + test.hand.getTotalPoints(partnerSummary) + " tpts");
                showSummary("My summary", result.state.players.me.infSummary);
                showSummary("Partner summary", partnerSummary);
                System.err.println("Test at " + result.where + " claims I should *not* have bid " + result.expected + " but I did");
                DebugUtils.debugMode = true;
                DebugUtils.breakpoint();
                test.getResult(bs);
                DebugUtils.debugMode = false;
            }
        }
        System.out.println("tests = " + bs.getTests().size());
        try(OutputStream os = new FileOutputStream(new File("/tmp/bids.txt"))) {
            bs.dump(os);
        }
        assertTrue(bs.getTests().size() > 0);
        assertFalse(hadError.get());
    }
}
