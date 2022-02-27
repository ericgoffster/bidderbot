package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Context;
import bbidder.Hand;
import bbidder.SimpleContext;

public class LongestOrEqualHigherRankingTest {
    @Test
    public void testValueOf() {
        assertEquals(new LongestOrEqualHigherRanking("s", "all"), LongestOrEqualHigherRanking.valueOf("longest_or_equal_higher_ranking s among all"));
        assertEquals(new LongestOrEqualHigherRanking("s", "all"), LongestOrEqualHigherRanking.valueOf("   longest_or_equal_higher_ranking    s    among    all    "));
        assertEquals(new LongestOrEqualHigherRanking("s", null), LongestOrEqualHigherRanking.valueOf("longest_or_equal_higher_ranking s"));
        assertEquals(new LongestOrEqualHigherRanking("s", null), LongestOrEqualHigherRanking.valueOf("    longest_or_equal_higher_ranking   s    "));
    }
    
    @Test
    public void testToString() {
        assertEquals("longest_or_equal_higher_ranking s among all", new LongestOrEqualHigherRanking("s", "all").toString());
        assertEquals("longest_or_equal_higher_ranking s", new LongestOrEqualHigherRanking("s", null).toString());
    }
    
    @Test
    public void testHigherRanking() {
        Context ctx = new SimpleContext();
        assertTrue(new LongestOrEqualHigherRanking("s", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqualHigherRanking("h", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqualHigherRanking("d", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqualHigherRanking("c", "all").matches(ctx, Hand.valueOf("AKQJ AKQJ 765 43")));
    }

    @Test
    public void testHigherRankingOfNotSpades() {
        Context ctx = new SimpleContext();
        assertTrue(new LongestOrEqualHigherRanking("h", "~S").matches(ctx, Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqualHigherRanking("d", "~S").matches(ctx, Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqualHigherRanking("c", "~S").matches(ctx, Hand.valueOf("AKQJ AKQJ 7654 3")));
    }
}
