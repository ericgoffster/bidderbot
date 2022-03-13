package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.InferenceContext;
import bbidder.Players;

public class SuitRangeTest {
    @Test
    public void testValueOf() {
        assertEquals(new SuitRange("s", 10, null), SuitRange.valueOf("10+ s"));
    }

    @Test
    public void testToString() {
        assertEquals("8-10 s", new SuitRange("s", 8, 10).toString());
    }

    @Test
    public void test() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new SuitRange("s", 3, 3).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange("s", 3, 4).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange("s", null, 3).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new SuitRange("s", 4, null).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQ JT9 876 5432")));

        assertFalse(new SuitRange("c", 3, 3).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange("c", 3, 4).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new SuitRange("c", 2, 3).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange("c", 4, null).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQ JT9 876 5432")));
    }
}
