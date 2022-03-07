package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import bbidder.inferences.Balanced;
import bbidder.inferences.HCPRange;

public class BidInferenceTest {
    @Test
    public void testValueOf() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(
                new BidInference(new BidPatternList(List.of(new BidPattern(false, "1N", true))),
                        new InferenceList(List.of(new HCPRange("15", "17"), new Balanced()))),
                BidInference.valueOf(reg, "1N => 15-17 hcp, balanced"));
    }

    @Test
    public void testToString() {
        assertEquals("1N => 15-17 hcp,balanced", new BidInference(new BidPatternList(List.of(new BidPattern(false, "1N", true))),
                new InferenceList(List.of(new HCPRange("15", "17"), new Balanced()))).toString());
    }

    @Test
    public void testBidContext() {
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
