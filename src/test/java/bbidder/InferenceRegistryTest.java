package bbidder;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import bbidder.inferences.Balanced;
import bbidder.inferences.HCPRange;
import bbidder.inferences.LongestOrEqual;
import bbidder.inferences.OpeningPreempt;
import bbidder.inferences.SuitRange;

public class InferenceRegistryTest {
    @Test
    public void testValueOf() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(new Balanced(), reg.valueOf("balanced"));
        assertEquals(new HCPRange(10, null), reg.valueOf("10+ hcp"));
        assertEquals(new LongestOrEqual("s", InferenceContext.lookupSuitSet("all"), null), reg.valueOf("longest_or_equal s among all"));
        assertEquals(new OpeningPreempt("S", 2), reg.valueOf("opening_preempt 2 S"));
        assertEquals(new SuitRange("s", 10, null), reg.valueOf("10+ s"));
    }
}
