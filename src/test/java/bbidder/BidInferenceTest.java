package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import bbidder.inferences.Balanced;
import bbidder.inferences.HCPRange;

public class BidInferenceTest {
    @Test
    public void testValueOf() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(
                new BidInference(new BidPatternList(List.of(new BidPattern(false, "1N", true))),
                        new InferenceList(List.of(new HCPRange(15, 17), new Balanced()))),
                BidInference.valueOf(reg, "1N => 15-17 hcp, balanced"));
    }

    @Test
    public void testToString() {
        assertEquals("1N => 15-17 hcp,balanced", new BidInference(new BidPatternList(List.of(new BidPattern(false, "1N", true))),
                new InferenceList(List.of(new HCPRange(15, 17), new Balanced()))).toString());
    }
}
