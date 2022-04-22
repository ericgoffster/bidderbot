package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.Players;

public class CombinedTotalPointsRangeTest {
    @Test
    public void testValueOf() {
        assertEquals(new CombinedTotalPointsRange(18, 21), CombinedTotalPointsRange.valueOf("min"));
        assertEquals(new CombinedTotalPointsRange(22, 24), CombinedTotalPointsRange.valueOf("inv"));
        assertEquals(new CombinedTotalPointsRange(25, 30), CombinedTotalPointsRange.valueOf("gf"));
        assertEquals(new CombinedTotalPointsRange(31, 32), CombinedTotalPointsRange.valueOf("slaminv"));
        assertEquals(new CombinedTotalPointsRange(33, 34), CombinedTotalPointsRange.valueOf("slam"));
        assertEquals(new CombinedTotalPointsRange(37, null), CombinedTotalPointsRange.valueOf("grand"));
    }

    @Test
    public void testToString() {
        assertEquals("8-10 combined tpts", new CombinedTotalPointsRange(8, 10).toString());
    }

    @Test
    public void testAffirmative() {
        assertTrue(new CombinedTotalPointsRange(10, 10).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new CombinedTotalPointsRange(10, null).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new CombinedTotalPointsRange(11, null).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new CombinedTotalPointsRange(null, 10).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new CombinedTotalPointsRange(null, 9).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
    }
}
