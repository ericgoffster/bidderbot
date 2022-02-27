package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Context;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.SimpleContext;

public class BalancedTest {
    @Test
    public void testValueOf() {
        assertEquals(new Balanced(), Balanced.valueOf("balanced"));
    }

    @Test
    public void testToString() {
        Context ctx = new SimpleContext();
        assertEquals("balanced", new Balanced().toString());
        assertEquals("balanced", new Balanced().bind(ctx).toString());
    }

    @Test
    public void test() {
        Context ctx = new SimpleContext();
        IBoundInference inf = new Balanced().bind(ctx);
        assertTrue(inf.matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(inf.matches(Hand.valueOf("AKQJT 98 765 432")));
        assertFalse(inf.matches(Hand.valueOf("AKQJT 98 76 5432")));
        assertFalse(inf.matches(Hand.valueOf("AKQJT 9 876 5432")));
        assertFalse(inf.matches(Hand.valueOf("AKQJT - 9876 5432")));
    }
}
