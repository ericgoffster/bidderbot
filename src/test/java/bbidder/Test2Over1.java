package bbidder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import bbidder.parsers.BiddingSystemParser;
import bbidder.parsers.CombinedPointsRangeParser;

public class Test2Over1 {
    public static BiddingSystem bs;

    public static String getWhere(BidInference i) {
        return i == null ? null : i.where;
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
                InfSummary partnerSummary = result.state.players.getPlayer(Position.PARTNER).infSummary;
                System.err.println("Bidding: " + result.bids);
                System.err
                        .println("You have " + test.hand + ": " + test.hand.numHCP() + " hcp: " + test.hand.getTotalPoints(partnerSummary) + " tpts");
                result.state.players.getPlayer(Position.ME).infSummary.showSummary("My summary");
                partnerSummary.showSummary("Partner summary");
                System.err.println("Test at " + result.where + " claims I should have bid " + result.expected);
                System.err.println("But I got an exception: ");
                result.ex.printStackTrace();
                test.getResult(bs);
            } else {
                String where = getWhere(result.found.matchedBid.inf);
                String where2 = getWhere(test.parent);
                if (!test.anti) {
                    if (!result.found.matchedBid.bid.bid.equals(result.expected)) {
                        hadError.set(true);
                        InfSummary partnerSummary = result.state.players.getPlayer(Position.PARTNER).infSummary;
                        System.err.println("Bidding: " + result.bids);
                        System.err.println(
                                "You have " + test.hand + ": " + test.hand.numHCP() + " hcp: " + test.hand.getTotalPoints(partnerSummary) + " tpts");
                        int combined = test.hand.getTotalPoints(partnerSummary) + partnerSummary.minTotalPts().orElse(0);
                        System.err.println("You have " + CombinedPointsRangeParser.getCombinedLabel(combined) + " combined tpts");
                        result.state.players.getPlayer(Position.ME).infSummary.showSummary("My summary");
                        partnerSummary.showSummary("Partner summary");
                        System.err.println("Test at " + result.where + " claims I should have bid " + result.expected);
                        if (result.found.matchedBid.inf != null) {
                            System.err.println("But " + where + " dictates I should bid " + result.found.matchedBid.bid);
                        } else {
                            System.err.println("But no systemic bid matched so " + result.found.matchedBid.bid + " was chosen");
                        }
                        System.err.println();
                        System.err.println("All bids matching the scenario in order of priority:");
                        for (PossibleBid b : result.found.getPossible()) {
                            if (b == result.found.matchedBid) {
                                System.err.println("   * " + b);
                            } else {
                                System.err.println("   " + b);
                            }
                            IBoundInference bi = b.inf.inferences.bind(result.state.players);
                            if (b == result.found.matchedBid) {
                                System.err.println("       * " + bi);

                            } else {
                                System.err.println("       " + bi);
                            }
                        }
                        // DebugUtils.debugMode = true;
                        // DebugUtils.breakpoint();
                        test.getResult(bs);
                        // DebugUtils.debugMode = false;
                    } else if (!Objects.equals(where, where2)) {
                        hadError.set(true);
                        System.err.println("Test at " + result.where + " does not match inference at " + where2);
                        if (where == null) {
                            System.err.println("but matches no inference at all");
                        } else {
                            System.err.println("but matches inference at " + where + " instead");
                        }
                        test.getResult(bs);
                    }
                } else if (result.found.matchedBid.bid.bid.equals(result.expected)) {
                    hadError.set(true);
                    InfSummary partnerSummary = result.state.players.getPlayer(Position.PARTNER).infSummary;
                    System.err.println(
                            "You have " + test.hand + ": " + test.hand.numHCP() + " hcp: " + test.hand.getTotalPoints(partnerSummary) + " tpts");
                    result.state.players.getPlayer(Position.ME).infSummary.showSummary("My summary");
                    partnerSummary.showSummary("Partner summary");
                    System.err.println("Test at " + result.where + " claims I should *not* have bid " + result.expected + " but I did");
                    test.getResult(bs);
                }
            }
        }
        System.out.println("tests = " + bs.getTests().size());
        try (OutputStream os = new FileOutputStream(new File("/tmp/bids.txt"))) {
            bs.dump(os);
        }
        assertTrue(bs.getTests().size() > 0);
        assertFalse(hadError.get());
    }
}
