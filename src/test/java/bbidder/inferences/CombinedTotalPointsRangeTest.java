package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InferenceContext;

public class CombinedTotalPointsRangeTest {
    @Test
    public void testValueOf() {
        assertEquals(new CombinedTotalPointsRange(19, 23), CombinedTotalPointsRange.valueOf("min"));
        assertEquals(new CombinedTotalPointsRange(24, 25), CombinedTotalPointsRange.valueOf("inv"));
        assertEquals(new CombinedTotalPointsRange(26, 30), CombinedTotalPointsRange.valueOf("gf"));
        assertEquals(new CombinedTotalPointsRange(31, 32), CombinedTotalPointsRange.valueOf("slaminv"));
        assertEquals(new CombinedTotalPointsRange(33, 36), CombinedTotalPointsRange.valueOf("slam"));
        assertEquals(new CombinedTotalPointsRange(37, null), CombinedTotalPointsRange.valueOf("grand"));
    }

    @Test
    public void testToString() {
        InferenceContext ctx = new InferenceContext();
        assertEquals("8-10 tpts", new CombinedTotalPointsRange(8, 10).toString());
        assertEquals("8-10 tpts", new CombinedTotalPointsRange(8, 10).bind(ctx).toString());
        assertEquals("8+ tpts", new CombinedTotalPointsRange(8, null).toString());
        assertEquals("8+ tpts", new CombinedTotalPointsRange(8, null).bind(ctx).toString());
        assertEquals("10- tpts", new CombinedTotalPointsRange(null, 10).toString());
        assertEquals("10- tpts", new CombinedTotalPointsRange(null, 10).bind(ctx).toString());
        assertEquals("10 tpts", new CombinedTotalPointsRange(10, 10).toString());
        assertEquals("10 tpts", new CombinedTotalPointsRange(10, 10).bind(ctx).toString());
    }
    
    @Test
    public void testReduce() {
        InferenceContext ctx = new InferenceContext();
        IBoundInference b1 = new CombinedTotalPointsRange(8, 10).bind(ctx);
        IBoundInference b2 = new CombinedTotalPointsRange(11, 12).bind(ctx);
        IBoundInference b3 = new CombinedTotalPointsRange(9, 15).bind(ctx);
        assertEquals("8-10 tpts", b1.orReduce(b1).toString());
        assertEquals("8-12 tpts", b1.orReduce(b2).toString());
        assertEquals("8-15 tpts", b1.orReduce(b3).toString());
        assertEquals("8-10 tpts", b1.andReduce(b1).toString());
        assertEquals("false", b1.andReduce(b2).toString());
        assertEquals("9-10 tpts", b1.andReduce(b3).toString());
    }

    @Test
    public void testAffirmative() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new CombinedTotalPointsRange(10, 10).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new CombinedTotalPointsRange(10, null).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new CombinedTotalPointsRange(11, null).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new CombinedTotalPointsRange(null, 10).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new CombinedTotalPointsRange(null, 9).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
    }

    @Test
    public void testNegative() {
        InferenceContext ctx = new InferenceContext();
        assertFalse(new CombinedTotalPointsRange(10, 10).bind(ctx).negate().matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new CombinedTotalPointsRange(10, null).bind(ctx).negate().matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new CombinedTotalPointsRange(11, null).bind(ctx).negate().matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new CombinedTotalPointsRange(null, 10).bind(ctx).negate().matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new CombinedTotalPointsRange(null, 9).bind(ctx).negate().matches(Hand.valueOf("AKQ JT9 876 5432")));
    }
}
