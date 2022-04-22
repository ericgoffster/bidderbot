package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import bbidder.inferences.AndInference;
import bbidder.inferences.Balanced;
import bbidder.inferences.SuitRange;

public class InferenceListTest {
    @Test
    public void testValueOf() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(new InferenceList(new Balanced()), InferenceList.valueOf(reg, "balanced"));
        assertEquals(new InferenceList(AndInference.create(List.of(new Balanced(), new SuitRange("S", 5, null)))), InferenceList.valueOf(reg, "balanced, 5+ S"));
    }

    @Test
    public void testToString() {
        assertEquals("balanced", new InferenceList(new Balanced()).toString());
        assertEquals("balanced,5+ S", new InferenceList(AndInference.create(List.of(new Balanced(), new SuitRange("S", 5, null)))).toString());
    }
}
