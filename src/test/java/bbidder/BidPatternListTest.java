package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class BidPatternListTest {
    @Test
    public void testValueOf() {
        assertEquals(new BidPatternList(List.of(new BidPattern(false, "1S", true, false, false))), BidPatternList.valueOf("1S"));
        assertEquals(new BidPatternList(List.of(new BidPattern(false, "1S", true, false, false), new BidPattern(false, "1N", true, false, false))),
                BidPatternList.valueOf("1S 1N"));
        assertEquals(new BidPatternList(List.of(new BidPattern(false, "1S", true, false, false), new BidPattern(false, "1N", false, false, false))),
                BidPatternList.valueOf("1S 1N:down"));
        assertEquals(new BidPatternList(List.of(new BidPattern(false, "1S", true, false, false), new BidPattern(true, "X", true, false, false),
                new BidPattern(false, "1N", true, false, false))), BidPatternList.valueOf("1S (X) 1N"));
    }

    @Test
    public void testToString() {
        assertEquals("1S", new BidPatternList(List.of(new BidPattern(false, "1S", true, false, false))).toString());
        assertEquals("1S:down", new BidPatternList(List.of(new BidPattern(false, "1S", false, false, false))).toString());
        assertEquals("1S 1N",
                new BidPatternList(List.of(new BidPattern(false, "1S", true, false, false), new BidPattern(false, "1N", true, false, false)))
                        .toString());
        assertEquals("1S (X) 1N", new BidPatternList(List.of(new BidPattern(false, "1S", true, false, false),
                new BidPattern(true, "X", true, false, false), new BidPattern(false, "1N", true, false, false))).toString());
    }

    BiddingContext makeBC(String str) {
        return new BiddingContext(BidList.valueOf(str), Map.of());
    }

    BiddingContext makeBC(String str, String sym, Integer v) {
        return new BiddingContext(BidList.valueOf(str), Map.of(sym, v));
    }

    List<BiddingContext> add2(BiddingContext... l) {
        List<BiddingContext> newL = new ArrayList<>();
        for (BiddingContext bc : l) {
            newL.add(new BiddingContext(bc.bids.withBidPrepended(Bid.P).withBidPrepended(Bid.P), bc.suits));
        }
        for (BiddingContext bc : l) {
            newL.add(new BiddingContext(bc.bids.withBidPrepended(Bid.P), bc.suits));
        }
        for (BiddingContext bc : l) {
            newL.add(bc);
        }
        return newL;
    }

    List<BiddingContext> add3(BiddingContext... l) {
        List<BiddingContext> newL = new ArrayList<>();
        for (BiddingContext bc : l) {
            newL.add(new BiddingContext(bc.bids.withBidPrepended(Bid.P).withBidPrepended(Bid.P).withBidPrepended(Bid.P), bc.suits));
        }
        for (BiddingContext bc : l) {
            newL.add(new BiddingContext(bc.bids.withBidPrepended(Bid.P).withBidPrepended(Bid.P), bc.suits));
        }
        for (BiddingContext bc : l) {
            newL.add(new BiddingContext(bc.bids.withBidPrepended(Bid.P), bc.suits));
        }
        for (BiddingContext bc : l) {
            newL.add(bc);
        }
        return newL;
    }

    @Test
    public void testBidContext() {
        assertEquals(BidPatternList.valueOf("(P) 1N").getContexts(), add2(makeBC("(P) 1N")));
        assertEquals(BidPatternList.valueOf("1N").getContexts(), add3(makeBC("1N")));
        assertEquals(BidPatternList.valueOf("1M").getContexts(), add3(makeBC("1H", "M", 2), makeBC("1S", "M", 3)));
        assertEquals(BidPatternList.valueOf("1M:down").getContexts(), add3(makeBC("1S", "M", 3), makeBC("1H", "M", 2)));
        assertEquals(BidPatternList.valueOf("1S (P) 1N").getContexts(), add3(makeBC("1S 1N")));
        assertEquals(BidPatternList.valueOf("1C NJM").getContexts(),
                add3(new BiddingContext(BidList.valueOf("1C 1H"), Map.of("M", 2)), new BiddingContext(BidList.valueOf("1C 1S"), Map.of("M", 3))));
        assertEquals(BidPatternList.valueOf("1C P").getContexts(), add3(new BiddingContext(BidList.valueOf("1C P"), Map.of())));
    }
}
