package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class BidPatternListTest {
    @Test
    public void testValueOf() {
        assertEquals(new BidPatternList(List.of(new BidPattern(false, "1S", true))), BidPatternList.valueOf("1S"));
        assertEquals(new BidPatternList(List.of(new BidPattern(false, "1S", true), new BidPattern(false, "1N", true))),
                BidPatternList.valueOf("1S 1N"));
        assertEquals(new BidPatternList(List.of(new BidPattern(false, "1S", true), new BidPattern(false, "1N", false))),
                BidPatternList.valueOf("1S 1N:down"));
        assertEquals(
                new BidPatternList(List.of(new BidPattern(false, "1S", true), new BidPattern(true, "X", true), new BidPattern(false, "1N", true))),
                BidPatternList.valueOf("1S (X) 1N"));
    }

    @Test
    public void testToString() {
        assertEquals("1S", new BidPatternList(List.of(new BidPattern(false, "1S", true))).toString());
        assertEquals("1S:down", new BidPatternList(List.of(new BidPattern(false, "1S", false))).toString());
        assertEquals("1S 1N", new BidPatternList(List.of(new BidPattern(false, "1S", true), new BidPattern(false, "1N", true))).toString());
        assertEquals("1S (X) 1N",
                new BidPatternList(List.of(new BidPattern(false, "1S", true), new BidPattern(true, "X", true), new BidPattern(false, "1N", true)))
                        .toString());
    }

    @Test
    public void testBidContext() {
        assertEquals(BidPatternList.valueOf("(P) 1N").getContexts(),
                List.of(new BiddingContext(BidList.valueOf("P 1N"), Map.of())));
        assertEquals(BidPatternList.valueOf("1N").getContexts(),
                List.of(new BiddingContext(BidList.valueOf("P 1N"), Map.of()), new BiddingContext(BidList.valueOf("1N"), Map.of())));
        assertEquals(BidPatternList.valueOf("1M").getContexts(),
                List.of(new BiddingContext(BidList.valueOf("P 1H"), Map.of("M", 2)), new BiddingContext(BidList.valueOf("P 1S"), Map.of("M", 3)),
                        new BiddingContext(BidList.valueOf("1H"), Map.of("M", 2)), new BiddingContext(BidList.valueOf("1S"), Map.of("M", 3))));
        assertEquals(BidPatternList.valueOf("1M:down").getContexts(),
                List.of(new BiddingContext(BidList.valueOf("P 1S"), Map.of("M", 3)), new BiddingContext(BidList.valueOf("P 1H"), Map.of("M", 2)),
                        new BiddingContext(BidList.valueOf("1S"), Map.of("M", 3)), new BiddingContext(BidList.valueOf("1H"), Map.of("M", 2))));
        assertEquals(BidPatternList.valueOf("1S (P) 1N").getContexts(),
                List.of(new BiddingContext(BidList.valueOf("P 1S P 1N"), Map.of()), new BiddingContext(BidList.valueOf("1S P 1N"), Map.of())));

        assertEquals(BidPatternList.valueOf("1C NJM").getContexts(), List.of(
                new BiddingContext(BidList.valueOf("P 1C P 1H"), Map.of("M", 2)), new BiddingContext(BidList.valueOf("P 1C P 1S"), Map.of("M", 3)),
                new BiddingContext(BidList.valueOf("1C P 1H"), Map.of("M", 2)), new BiddingContext(BidList.valueOf("1C P 1S"), Map.of("M", 3))));
        assertEquals(BidPatternList.valueOf("1C P").getContexts(),
                List.of(new BiddingContext(BidList.valueOf("P 1C P P"), Map.of()), new BiddingContext(BidList.valueOf("1C P P"), Map.of())));
    }
}
