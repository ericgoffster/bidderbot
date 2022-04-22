package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.SuitSets;
import bbidder.SuitSets.SuitSet;
import bbidder.Players;

public class LongestOrEqualTest {
    SuitSet ALL = SuitSets.lookupSuitSet("ALL");

    @Test
    public void testValueOf() {
        assertEquals(new LongestOrEqual("s", ALL), LongestOrEqual.valueOf("longest_or_equal s among all"));
        assertEquals(new LongestOrEqual("s", ALL), LongestOrEqual.valueOf("   longest_or_equal    s    among    all    "));
        assertEquals(new LongestOrEqual("s", null), LongestOrEqual.valueOf("longest_or_equal s"));
        assertEquals(new LongestOrEqual("s", null), LongestOrEqual.valueOf("    longest_or_equal  s    "));
    }

    @Test
    public void testToString() {
        assertEquals("longest_or_equal x among ALL", new LongestOrEqual("x", ALL).toString());
        assertEquals("longest_or_equal s", new LongestOrEqual("s", null).toString());
    }

    @Test
    public void testHigherRanking() {
        assertTrue(new LongestOrEqual("s", ALL).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqual("h", ALL).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("d", ALL).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("c", ALL).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
    }

    @Test
    public void testHigherRankingOfNotSpades() {
        assertTrue(new LongestOrEqual("h", SuitSets.lookupSuitSet("~S")).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertTrue(new LongestOrEqual("d", SuitSets.lookupSuitSet("~S")).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqual("c", SuitSets.lookupSuitSet("~S")).bind(new Players()).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
    }
}
