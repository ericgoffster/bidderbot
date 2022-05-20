package bbidder;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import bbidder.inferences.Balanced;
import bbidder.inferences.HCPRange;
import bbidder.inferences.LongestOrEqual;
import bbidder.inferences.SuitRange;
import bbidder.symbols.ConstSymbol;

public class InferenceRegistryTest {
    @Test
    public void testValueOf() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(Balanced.BALANCED, reg.parseInference("balanced"));
        assertEquals(new HCPRange(10, null), reg.parseInference("10+ hcp"));
        assertEquals(new LongestOrEqual(new ConstSymbol(3), SuitSetParser.lookupSuitSet("all")), reg.parseInference("longest_or_equal s among all"));
        assertEquals(new SuitRange(new ConstSymbol(3), 10, null), reg.parseInference("10+ s"));
    }
}
