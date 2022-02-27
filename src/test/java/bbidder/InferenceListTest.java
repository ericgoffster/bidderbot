package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import bbidder.inferences.Balanced;
import bbidder.inferences.SuitRange;

public class InferenceListTest {
    @Test
    public void testValueOf() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();        
        assertEquals(new InferenceList(List.of(new Balanced())), InferenceList.valueOf(reg, "balanced"));
        assertEquals(new InferenceList(List.of(new Balanced(), new SuitRange("S", "5", null))), InferenceList.valueOf(reg, "balanced, 5+ in S"));
    }
    @Test
    public void testToString() {
        assertEquals("balanced", new InferenceList(List.of(new Balanced())).toString());
        assertEquals("balanced,5+ in S", new InferenceList(List.of(new Balanced(), new SuitRange("S", "5", null))).toString());
    }
}
