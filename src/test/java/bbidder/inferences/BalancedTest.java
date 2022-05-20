package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Players;

public class BalancedTest {
    @Test
    public void testValueOf() {
        assertEquals(Balanced.BALANCED, Balanced.valueOf("balanced"));
    }

    @Test
    public void testToString() {
        assertEquals("balanced", Balanced.BALANCED.toString());
    }

    @Test
    public void testAffirmative() {
        IBoundInference inf = Balanced.BALANCED.bind(new Players());
        assertTrue(inf.test(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(inf.test(Hand.valueOf("AKQJT 98 765 432")));
        assertFalse(inf.test(Hand.valueOf("AKQJT 98 76 5432")));
        assertFalse(inf.test(Hand.valueOf("AKQJT 9 876 5432")));
        assertFalse(inf.test(Hand.valueOf("AKQJT - 9876 5432")));
    }
}
