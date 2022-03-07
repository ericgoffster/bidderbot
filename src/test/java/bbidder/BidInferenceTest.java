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
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(BidInference.valueOf(reg, "1N => balanced").getContexts(),
                List.of(new BidCtx(BidList.valueOf("P 1N"), Bid._1N, Map.of()), new BidCtx(BidList.valueOf("1N"), Bid._1N, Map.of())));
        assertEquals(BidInference.valueOf(reg, "1M => balanced").getContexts(),
                List.of(
                        new BidCtx(BidList.valueOf("P 1H"), Bid._1H, Map.of("M", 2)),
                        new BidCtx(BidList.valueOf("P 1S"), Bid._1S, Map.of("M", 3)),
                        new BidCtx(BidList.valueOf("1H"), Bid._1H, Map.of("M", 2)),
                        new BidCtx(BidList.valueOf("1S"), Bid._1S, Map.of("M", 3))));
        assertEquals(BidInference.valueOf(reg, "1M:down => balanced").getContexts(),
                List.of(
                        new BidCtx(BidList.valueOf("P 1S"), Bid._1S, Map.of("M", 3)),
                        new BidCtx(BidList.valueOf("P 1H"), Bid._1H, Map.of("M", 2)),
                        new BidCtx(BidList.valueOf("1S"), Bid._1S, Map.of("M", 3)),
                        new BidCtx(BidList.valueOf("1H"), Bid._1H, Map.of("M", 2))));
        assertEquals(BidInference.valueOf(reg, "1S (P) 1N => balanced").getContexts(),
                List.of(
                        new BidCtx(BidList.valueOf("P 1S P 1N"), Bid._1N, Map.of()),
                        new BidCtx(BidList.valueOf("1S P 1N"), Bid._1N, Map.of())));

        assertEquals(BidInference.valueOf(reg, "1C NJM => balanced").getContexts(),
                List.of(
                        new BidCtx(BidList.valueOf("P 1C P 1H"), Bid._1H, Map.of("M", 2)),
                        new BidCtx(BidList.valueOf("P 1C P 1S"), Bid._1S, Map.of("M", 3)),
                        new BidCtx(BidList.valueOf("1C P 1H"), Bid._1H, Map.of("M", 2)),
                        new BidCtx(BidList.valueOf("1C P 1S"), Bid._1S, Map.of("M", 3))));
        assertEquals(BidInference.valueOf(reg, "1C P => balanced").getContexts(),
                List.of(
                        new BidCtx(BidList.valueOf("P 1C P P"), Bid._1C, Map.of()),
                        new BidCtx(BidList.valueOf("1C P P"), Bid._1C, Map.of())));
    }
}
