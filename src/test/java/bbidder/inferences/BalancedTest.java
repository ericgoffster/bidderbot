package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InferenceContext;
import bbidder.Players;

public class BalancedTest {
    @Test
    public void testValueOf() {
        assertEquals(new Balanced(), Balanced.valueOf("balanced"));
    }

    @Test
    public void testToString() {
        assertEquals("balanced", new Balanced().toString());
    }

    @Test
    public void testAffirmative() {
        InferenceContext ctx = new InferenceContext();
        IBoundInference inf = new Balanced().bind(ctx).get(0).inf;
        assertTrue(inf.matches(new Players(), Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(inf.matches(new Players(), Hand.valueOf("AKQJT 98 765 432")));
        assertFalse(inf.matches(new Players(), Hand.valueOf("AKQJT 98 76 5432")));
        assertFalse(inf.matches(new Players(), Hand.valueOf("AKQJT 9 876 5432")));
        assertFalse(inf.matches(new Players(), Hand.valueOf("AKQJT - 9876 5432")));
    }
}
