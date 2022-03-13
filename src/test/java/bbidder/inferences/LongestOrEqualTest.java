package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.InferenceContext;
import bbidder.Players;

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
        assertEquals("longest_or_equal x among all", new LongestOrEqual("x", "all").toString());
        assertEquals("longest_or_equal s", new LongestOrEqual("s", null).toString());
    }

    @Test
    public void testHigherRanking() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new LongestOrEqual("s", "all").bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqual("h", "all").bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("d", "all").bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("c", "all").bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQJ AKQJ 765 43")));
    }

    @Test
    public void testHigherRankingOfNotSpades() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new LongestOrEqual("h", "~S").bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertTrue(new LongestOrEqual("d", "~S").bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqual("c", "~S").bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("AKQJ AKQJ 7654 3")));
    }
}
