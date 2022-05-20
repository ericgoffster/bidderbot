package bbidder;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import bbidder.inferences.AndInference;
import bbidder.inferences.Balanced;
import bbidder.inferences.HCPRange;

public class BidInferenceTest {
    @Test
    public void testValueOf() {
        assertEquals(new BidInference("System", BidPatternList.EMPTY.withBidAdded(BidPattern.createSimpleBid(Bid._1N)),
                AndInference.create(new HCPRange(15, 17), Balanced.BALANCED)), BidInference.valueOf(null, "1N => 15-17 hcp, balanced"));
    }

    @Test
    public void testToString() {
        assertEquals("1N => 15-17 hcp,balanced", new BidInference("System", BidPatternList.EMPTY.withBidAdded(BidPattern.createSimpleBid(Bid._1N)),
                AndInference.create(new HCPRange(15, 17), Balanced.BALANCED)).toString());
    }
}
