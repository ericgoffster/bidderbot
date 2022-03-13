package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import bbidder.BiddingContext;
import bbidder.BidList;
import bbidder.Hand;
import bbidder.InferenceContext;
import bbidder.LikelyHands;

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
        BiddingContext bc = new BiddingContext(new BidList(List.of()), Map.of("x", 1));
        InferenceContext ctx = new InferenceContext(null, new LikelyHands(), bc);
        assertEquals("longest_or_equal x among all", new LongestOrEqual("x", "all").toString());
        assertEquals("longest_or_equal D", new LongestOrEqual("x", "all").bind(ctx).toString());
        assertEquals("longest_or_equal s", new LongestOrEqual("s", null).toString());
        assertEquals("longest_or_equal S", new LongestOrEqual("s", null).bind(ctx).toString());
    }

    @Test
    public void testHigherRanking() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new LongestOrEqual("s", "all").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqual("h", "all").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("d", "all").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("c", "all").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 765 43")));
    }

    @Test
    public void testHigherRankingNegate() {
        InferenceContext ctx = new InferenceContext();
        assertFalse(new LongestOrEqual("s", "all").bind(ctx).negate().matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual("h", "all").bind(ctx).negate().matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqual("d", "all").bind(ctx).negate().matches(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqual("c", "all").bind(ctx).negate().matches(Hand.valueOf("AKQJ AKQJ 765 43")));
    }

    @Test
    public void testHigherRankingOfNotSpades() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new LongestOrEqual("h", "~S").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertTrue(new LongestOrEqual("d", "~S").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqual("c", "~S").bind(ctx).matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
    }

    @Test
    public void testHigherRankingOfNotSpadesNegate() {
        InferenceContext ctx = new InferenceContext();
        assertFalse(new LongestOrEqual("h", "~S").bind(ctx).negate().matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertFalse(new LongestOrEqual("d", "~S").bind(ctx).negate().matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
        assertTrue(new LongestOrEqual("c", "~S").bind(ctx).negate().matches(Hand.valueOf("AKQJ AKQJ 7654 3")));
    }
}
