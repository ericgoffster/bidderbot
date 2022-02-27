package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Context;
import bbidder.Hand;
import bbidder.SimpleContext;

public class SuitRangeTest {
    @Test
    public void testValueOf() {
        assertEquals(new SuitRange("s", "10", null), SuitRange.valueOf("10+ in s"));
    }

    @Test
    public void testToString() {
        Context ctx = new SimpleContext();
        assertEquals("8-10 in s", new SuitRange("s", "8", "10").toString());
        assertEquals("8-10 in S", new SuitRange("s", "8", "10").bind(ctx).toString());
    }

    @Test
    public void test() {
        Context ctx = new SimpleContext();
        assertTrue(new SuitRange("s", "3", "3").bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange("s", "3", "4").bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange("s", null, "3").bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new SuitRange("s", "4", null).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));

        assertFalse(new SuitRange("c", "3", "3").bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange("c", "3", "4").bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new SuitRange("c", "2", "3").bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange("c", "4", null).bind(ctx).matches(Hand.valueOf("AKQ JT9 876 5432")));
    }
}
