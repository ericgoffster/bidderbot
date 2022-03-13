package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InferenceContext;

public class BalancedTest {
    @Test
    public void testValueOf() {
        assertEquals(new Balanced(), Balanced.valueOf("balanced"));
    }

    @Test
    public void testToString() {
        InferenceContext ctx = new InferenceContext();
        assertEquals("balanced", new Balanced().toString());
        assertEquals("balanced", new Balanced().bind(ctx).toString());
        assertEquals("unbalanced", new Balanced().bind(ctx).negate().toString());
    }

    @Test
    public void testReduce() {
        InferenceContext ctx = new InferenceContext();
        IBoundInference b1 = new Balanced().bind(ctx);
        IBoundInference b2 = new Balanced().bind(ctx).negate();
        IBoundInference b3 = new OpeningPreempt("S", 2).bind(ctx);
        assertEquals(b1.andReduce(b3), ConstBoundInference.F);
        assertEquals(b2.andReduce(b3), b3);
        assertEquals(b1.andReduce(b1), b1);
        assertEquals(b1.andReduce(b2), ConstBoundInference.F);
        assertEquals(b2.andReduce(b1), ConstBoundInference.F);
        assertEquals(b1.orReduce(b1), b1);
        assertEquals(b1.orReduce(b2), ConstBoundInference.T);
        assertEquals(b2.orReduce(b1), ConstBoundInference.T);
    }

    @Test
    public void testAffirmative() {
        InferenceContext ctx = new InferenceContext();
        IBoundInference inf = new Balanced().bind(ctx);
        assertTrue(inf.matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(inf.matches(Hand.valueOf("AKQJT 98 765 432")));
        assertFalse(inf.matches(Hand.valueOf("AKQJT 98 76 5432")));
        assertFalse(inf.matches(Hand.valueOf("AKQJT 9 876 5432")));
        assertFalse(inf.matches(Hand.valueOf("AKQJT - 9876 5432")));
    }

    @Test
    public void testNegative() {
        InferenceContext ctx = new InferenceContext();
        IBoundInference inf = new Balanced().bind(ctx).negate();
        assertFalse(inf.matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(inf.matches(Hand.valueOf("AKQJT 98 765 432")));
        assertTrue(inf.matches(Hand.valueOf("AKQJT 98 76 5432")));
        assertTrue(inf.matches(Hand.valueOf("AKQJT 9 876 5432")));
        assertTrue(inf.matches(Hand.valueOf("AKQJT - 9876 5432")));
    }
}
