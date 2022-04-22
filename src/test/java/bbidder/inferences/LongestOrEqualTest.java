package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.BiddingContext;
import bbidder.Hand;
import bbidder.InferenceContext;
import bbidder.InferenceContext.SuitSet;
import bbidder.Players;

public class LongestOrEqualTest {
    SuitSet ALL = InferenceContext.lookupSuitSet("ALL");
    @Test
    public void testValueOf() {
        assertEquals(new LongestOrEqual("s", ALL, null), LongestOrEqual.valueOf("longest_or_equal s among all"));
        assertEquals(new LongestOrEqual("s", ALL, null), LongestOrEqual.valueOf("   longest_or_equal    s    among    all    "));
        assertEquals(new LongestOrEqual("s", null, null), LongestOrEqual.valueOf("longest_or_equal s"));
        assertEquals(new LongestOrEqual("s", null, null), LongestOrEqual.valueOf("    longest_or_equal  s    "));
    }

    @Test
    public void testToString() {
        assertEquals("longest_or_equal x among ALL", new LongestOrEqual("x", ALL, null).toString());
        assertEquals("longest_or_equal s", new LongestOrEqual("s", null, null).toString());
    }

    @Test
    public void testHigherRanking() {
        assertTrue(new LongestOrEqual("s", ALL, null).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqual("h", ALL, null).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("d", ALL, null).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("c", ALL, null).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
    }

    @Test
    public void testHigherRankingOfNotSpades() {
        assertTrue(new LongestOrEqual("h", InferenceContext.lookupSuitSet("~S"), BiddingContext.EMPTY).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertTrue(new LongestOrEqual("d", InferenceContext.lookupSuitSet("~S"), BiddingContext.EMPTY).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqual("c", InferenceContext.lookupSuitSet("~S"), BiddingContext.EMPTY).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
    }
}
