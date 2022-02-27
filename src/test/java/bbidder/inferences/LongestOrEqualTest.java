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
        assertEquals(new LongestOrEqual("s", "all"),
                LongestOrEqual.valueOf("   longest_or_equal    s    among    all    "));
        assertEquals(new LongestOrEqual("s", null), LongestOrEqual.valueOf("longest_or_equal s"));
        assertEquals(new LongestOrEqual("s", null), LongestOrEqual.valueOf("    longest_or_equal  s    "));
    }

    @Test
    public void testToString() {
        assertEquals("longest_or_equal s among all", new LongestOrEqual("s", "all").toString());
        assertEquals("longest_or_equal s", new LongestOrEqual("s", null).toString());
    }

    @Test
    public void testHigherRanking() {
        Context ctx = new SimpleContext();
        assertTrue(new LongestOrEqual("s", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqual("h", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("d", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("c", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
    }

    @Test
    public void testHigherRankingOfNotSpades() {
        Context ctx = new SimpleContext();
        assertTrue(new LongestOrEqual("h", "~S").matches(ctx, Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertTrue(new LongestOrEqual("d", "~S").matches(ctx, Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqual("c", "~S").matches(ctx, Hand.valueOf("AKQJ AKQJ 7654 3")));
    }
}
