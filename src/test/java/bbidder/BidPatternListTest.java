package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class BidPatternListTest {
    @Test
    public void testValueOf() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S)), BidPatternList.valueOf(reg, "1S").getBids());
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S), BidPattern.createSimpleBid(Bid._1N)), BidPatternList.valueOf(reg, "1S 1N").getBids());
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S), BidPattern.createSimpleBid(Bid.X).withIsOpposition(true), BidPattern.createSimpleBid(Bid._1N)),
                BidPatternList.valueOf(reg, "1S (X) 1N").getBids());
    }

    @Test
    public void testToString() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals("1S", BidPatternList.valueOf(reg, "1S").toString());
        assertEquals("1S 1N", BidPatternList.valueOf(reg, "1S 1N").toString());
        assertEquals("1S (X) 1N", BidPatternList.valueOf(reg, "1S (X) 1N").toString());
    }
}
