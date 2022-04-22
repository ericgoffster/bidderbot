package bbidder;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import bbidder.inferences.Balanced;
import bbidder.inferences.HCPRange;

public class BidInferenceTest {
    @Test
    public void testValueOf() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(BidInference.EMPTY.withBidAdded(BidPattern.createSimpleBid(Bid._1N))
                .withInferenceAdded(new HCPRange(15, 17))
                .withInferenceAdded(new Balanced()), BidInference.valueOf(null, reg, "1N => 15-17 hcp, balanced"));
    }

    @Test
    public void testToString() {
        assertEquals("1N => 15-17 hcp,balanced",
                BidInference.EMPTY.withBidAdded(BidPattern.createSimpleBid(Bid._1N))
                        .withInferenceAdded(new HCPRange(15, 17))
                        .withInferenceAdded(new Balanced())
                        .toString());
    }
}
