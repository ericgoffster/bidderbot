package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BidPatternListTest {
    @Test
    public void testValueOf() {
        assertEquals(List.of(BidPattern.valueOf("1S")), BidPatternList.valueOf("1S").getBids());
        assertEquals(List.of(BidPattern.valueOf("1S"), BidPattern.valueOf("1N")), BidPatternList.valueOf("1S 1N").getBids());
        assertEquals(List.of(BidPattern.valueOf("1S"), BidPattern.valueOf("(X)"), BidPattern.valueOf("1N")),
                BidPatternList.valueOf("1S (X) 1N").getBids());
    }

    @Test
    public void testToString() {
        assertEquals("1S", BidPatternList.valueOf("1S").toString());
        assertEquals("1S 1N", BidPatternList.valueOf("1S 1N").toString());
        assertEquals("1S (X) 1N", BidPatternList.valueOf("1S (X) 1N").toString());
    }

    List<BiddingContext> add2(BiddingContext... l) {
        List<BiddingContext> newL = new ArrayList<>();
        for (BiddingContext bc : l) {
            newL.add(bc.withBidPrepended(Bid.P).withBidPrepended(Bid.P));
        }
        for (BiddingContext bc : l) {
            newL.add(bc.withBidPrepended(Bid.P));
        }
        for (BiddingContext bc : l) {
            newL.add(bc);
        }
        return newL;
    }

    List<BiddingContext> add3(BiddingContext... l) {
        List<BiddingContext> newL = new ArrayList<>();
        for (BiddingContext bc : l) {
            newL.add(bc.withBidPrepended(Bid.P).withBidPrepended(Bid.P).withBidPrepended(Bid.P));
        }
        for (BiddingContext bc : l) {
            newL.add(bc.withBidPrepended(Bid.P).withBidPrepended(Bid.P));
        }
        for (BiddingContext bc : l) {
            newL.add(bc.withBidPrepended(Bid.P));
        }
        for (BiddingContext bc : l) {
            newL.add(bc);
        }
        return newL;
    }

    @Test
    public void testBidContext() {
        assertEquals(BidPatternList.valueOf("(P) 1N").getContexts(), add2(BiddingContext.EMPTY.withBidAdded(Bid.P).withBidAdded(Bid._1N)));
        assertEquals(BidPatternList.valueOf("1N").getContexts(), add3(BiddingContext.EMPTY.withBidAdded(Bid._1N)));
        assertEquals(BidPatternList.valueOf("1M").getContexts(), add3(BiddingContext.EMPTY.withNewBid(Bid._1H, BidPattern.valueOf("1M")),
                BiddingContext.EMPTY.withNewBid(Bid._1S, BidPattern.valueOf("1M"))));
        assertEquals(BidPatternList.valueOf("1S (P) 1N").getContexts(),
                add3(BiddingContext.EMPTY.withBidAdded(Bid._1S).withBidAdded(Bid.P).withBidAdded(Bid._1N)));
        assertEquals(BidPatternList.valueOf("1C NJM").getContexts(),
                add3(BiddingContext.EMPTY.withBidAdded(Bid._1C).withBidAdded(Bid.P).withNewBid(Bid._1H, BidPattern.valueOf("NJM")),
                        BiddingContext.EMPTY.withBidAdded(Bid._1C).withBidAdded(Bid.P).withNewBid(Bid._1S, BidPattern.valueOf("NJM"))));
        assertEquals(BidPatternList.valueOf("1C P").getContexts(),
                add3(BiddingContext.EMPTY.withBidAdded(Bid._1C).withBidAdded(Bid.P).withBidAdded(Bid.P)));
    }
}
