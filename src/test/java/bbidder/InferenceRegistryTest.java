package bbidder;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import bbidder.inferences.Balanced;
import bbidder.inferences.HCPRange;
import bbidder.inferences.LongestOrEqualHigherRanking;
import bbidder.inferences.LongestOrEqualLowerRanking;
import bbidder.inferences.OpeningPreempt;
import bbidder.inferences.SuitRange;

public class InferenceRegistryTest {
    @Test
    public void testValueOf() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(new Balanced(), reg.valueOf("balanced"));
        assertEquals(new HCPRange("10", null), reg.valueOf("10+hcp"));
        assertEquals(new LongestOrEqualHigherRanking("s", "all"), reg.valueOf("longest_or_equal_higher_ranking s among all"));
        assertEquals(new LongestOrEqualLowerRanking("s", "all"), reg.valueOf("longest_or_equal_lower_ranking s among all"));
        assertEquals(new OpeningPreempt("S", 2), reg.valueOf("opening_preempt 2 S"));
        assertEquals(new SuitRange("s", "10", null), reg.valueOf("10+ in s"));
    }
}
