package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Context;
import bbidder.Hand;
import bbidder.SimpleContext;

public class LongestOrEqualLowerRankingTest {
    @Test
    public void testValueOf() {
        assertEquals(new LongestOrEqualLowerRanking("s", "all"), LongestOrEqualLowerRanking.valueOf("longest_or_equal_lower_ranking s among all"));
        assertEquals(new LongestOrEqualLowerRanking("s", "all"),
                LongestOrEqualLowerRanking.valueOf("   longest_or_equal_lower_ranking    s    among    all    "));
        assertEquals(new LongestOrEqualLowerRanking("s", null), LongestOrEqualLowerRanking.valueOf("longest_or_equal_lower_ranking s"));
        assertEquals(new LongestOrEqualLowerRanking("s", null), LongestOrEqualLowerRanking.valueOf("    longest_or_equal_lower_ranking   s    "));
    }

    @Test
    public void testToString() {
        assertEquals("longest_or_equal_lower_ranking s among all", new LongestOrEqualLowerRanking("s", "all").toString());
        assertEquals("longest_or_equal_lower_ranking s", new LongestOrEqualLowerRanking("s", null).toString());
    }

    @Test
    public void testLowerRanking() {
        Context ctx = new SimpleContext();
        assertFalse(new LongestOrEqualLowerRanking("s", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqualLowerRanking("h", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqualLowerRanking("d", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqualLowerRanking("c", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
    }

    @Test
    public void testLowerRankingOfNotHearts() {
        Context ctx = new SimpleContext();
        assertFalse(new LongestOrEqualLowerRanking("s", "~H").matches(ctx, Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertTrue(new LongestOrEqualLowerRanking("d", "~H").matches(ctx, Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqualLowerRanking("c", "~H").matches(ctx, Hand.valueOf("AKQJ AKQJ 7654 3")));
    }
}
