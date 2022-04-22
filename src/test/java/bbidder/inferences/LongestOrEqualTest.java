package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.BiddingContext;
import bbidder.Hand;
import bbidder.Players;

public class LongestOrEqualTest {
    @Test
    public void testValueOf() {
        assertEquals(new LongestOrEqual("s", "all", null), LongestOrEqual.valueOf("longest_or_equal s among all"));
        assertEquals(new LongestOrEqual("s", "all", null), LongestOrEqual.valueOf("   longest_or_equal    s    among    all    "));
        assertEquals(new LongestOrEqual("s", null, null), LongestOrEqual.valueOf("longest_or_equal s"));
        assertEquals(new LongestOrEqual("s", null, null), LongestOrEqual.valueOf("    longest_or_equal  s    "));
    }

    @Test
    public void testToString() {
        assertEquals("longest_or_equal x among all", new LongestOrEqual("x", "all", null).toString());
        assertEquals("longest_or_equal s", new LongestOrEqual("s", null, null).toString());
    }

    @Test
    public void testHigherRanking() {
        assertTrue(new LongestOrEqual("s", "all", null).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqual("h", "all", null).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("d", "all", null).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("c", "all", null).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
    }

    @Test
    public void testHigherRankingOfNotSpades() {
        assertTrue(new LongestOrEqual("h", "~S", BiddingContext.EMPTY).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertTrue(new LongestOrEqual("d", "~S", BiddingContext.EMPTY).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqual("c", "~S", BiddingContext.EMPTY).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
    }
}
