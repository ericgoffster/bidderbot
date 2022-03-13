package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InferenceContext;

public class HCPRangeTest {
    @Test
    public void testValueOf() {
        assertEquals(new HCPRange(10, null), HCPRange.valueOf("10+ hcp"));
        assertEquals(new HCPRange(10, null), HCPRange.valueOf("  10+  Hcp    "));
        assertEquals(new HCPRange(null, 10), HCPRange.valueOf("10- hcp"));
        assertEquals(new HCPRange(null, 10), HCPRange.valueOf("  10-  Hcp    "));
        assertEquals(new HCPRange(8, 10), HCPRange.valueOf("8-10 hcp"));
        assertEquals(new HCPRange(8, 10), HCPRange.valueOf("  8-10    Hcp    "));
        assertEquals(new HCPRange(8, 8), HCPRange.valueOf("  8    Hcp    "));
    }

    @Test
    public void testToString() {
        InferenceContext ctx = new InferenceContext();
        assertEquals("8-10 hcp", new HCPRange(8, 10).toString());
        assertEquals("8-10 hcp", new HCPRange(8, 10).bind(ctx).toString());
        assertEquals("8+ hcp", new HCPRange(8, null).toString());
        assertEquals("8+ hcp", new HCPRange(8, null).bind(ctx).toString());
        assertEquals("10- hcp", new HCPRange(null, 10).toString());
        assertEquals("10- hcp", new HCPRange(null, 10).bind(ctx).toString());
        assertEquals("10 hcp", new HCPRange(10, 10).toString());
        assertEquals("10 hcp", new HCPRange(10, 10).bind(ctx).toString());
    }

    @Test
    public void testReduce() {
        InferenceContext ctx = new InferenceContext();
        IBoundInference b1 = new HCPRange(8, 10).bind(ctx);
        IBoundInference b2 = new HCPRange(11, 12).bind(ctx);
        IBoundInference b3 = new HCPRange(9, 15).bind(ctx);
        assertEquals("8-10 hcp", b1.orReduce(b1).toString());
        assertEquals("8-12 hcp", b1.orReduce(b2).toString());
        assertEquals("8-15 hcp", b1.orReduce(b3).toString());
        assertEquals("8-10 hcp", b1.andReduce(b1).toString());
        assertEquals("false", b1.andReduce(b2).toString());
        assertEquals("9-10 hcp", b1.andReduce(b3).toString());
    }

    @Test
    public void testAffirmative() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new HCPRange(10, 10).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new HCPRange(10, null).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new HCPRange(11, null).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new HCPRange(null, 10).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new HCPRange(null, 9).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
    }

    @Test
    public void testNegative() {
        InferenceContext ctx = new InferenceContext();
        assertFalse(new HCPRange(10, 10).bind(ctx).negate().matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new HCPRange(10, null).bind(ctx).negate().matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new HCPRange(11, null).bind(ctx).negate().matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new HCPRange(null, 10).bind(ctx).negate().matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new HCPRange(null, 9).bind(ctx).negate().matches(Hand.valueOf("AKQ JT9 876 5432")));
    }
}
