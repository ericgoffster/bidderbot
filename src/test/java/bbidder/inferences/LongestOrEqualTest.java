package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Context;
import bbidder.Hand;
import bbidder.SimpleContext;

public class LongestOrEqualTest {
    @Test
    public void testValueOf() {
        assertEquals(new LongestOrEqual("s", "all"), LongestOrEqual.valueOf("longest_or_equal s among all"));
        assertEquals(new LongestOrEqual("s", "all"), LongestOrEqual.valueOf("   longest_or_equal    s    among    all    "));
        assertEquals(new LongestOrEqual("s", null), LongestOrEqual.valueOf("longest_or_equal s"));
        assertEquals(new LongestOrEqual("s", null), LongestOrEqual.valueOf("    longest_or_equal  s    "));
    }

    @Test
    public void testToString() {
        Context ctx = new SimpleContext(s -> 1);
        assertEquals("longest_or_equal x among all", new LongestOrEqual("x", "all").toString());
        assertEquals("longest_or_equal D", new LongestOrEqual("x", "all").bind(ctx).toString());
        assertEquals("longest_or_equal s", new LongestOrEqual("s", null).toString());
        assertEquals("longest_or_equal S", new LongestOrEqual("s", null).bind(ctx).toString());
    }

    @Test
    public void testHigherRanking() {
        Context ctx = new SimpleContext();
        assertTrue(new LongestOrEqual("s", "all").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqual("h", "all").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("d", "all").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("c", "all").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
    }

    @Test
    public void testHigherRankingOfNotSpades() {
        Context ctx = new SimpleContext();
        assertTrue(new LongestOrEqual("h", "~S").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertTrue(new LongestOrEqual("d", "~S").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqual("c", "~S").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
    }
}