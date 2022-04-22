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
        assertEquals(new Balanced(), reg.parseInference("balanced"));
        assertEquals(new HCPRange(10, null), reg.parseInference("10+ hcp"));
        assertEquals(new LongestOrEqual("s", SuitSets.lookupSuitSet("all")), reg.parseInference("longest_or_equal s among all"));
        assertEquals(new OpeningPreempt("S", 2), reg.parseInference("opening_preempt 2 S"));
        assertEquals(new SuitRange("s", 10, null), reg.parseInference("10+ s"));
    }
}
