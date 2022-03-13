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
        assertEquals(new Balanced(BalanceType.regular), Balanced.valueOf("balanced"));
    }

    @Test
    public void testToString() {
        assertEquals("balanced", new Balanced(BalanceType.regular).toString());
    }

    @Test
    public void testAffirmative() {
        InferenceContext ctx = new InferenceContext();
        IBoundInference inf = new Balanced(BalanceType.regular).bind(ctx).get(0).inf;
        assertTrue(inf.matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(inf.matches(Hand.valueOf("AKQJT 98 765 432")));
        assertFalse(inf.matches(Hand.valueOf("AKQJT 98 76 5432")));
        assertFalse(inf.matches(Hand.valueOf("AKQJT 9 876 5432")));
        assertFalse(inf.matches(Hand.valueOf("AKQJT - 9876 5432")));
    }
}
