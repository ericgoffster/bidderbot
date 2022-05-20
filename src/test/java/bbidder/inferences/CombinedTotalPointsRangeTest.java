package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Players;
import bbidder.parsers.HandParser;

public class CombinedTotalPointsRangeTest {
    @Test
    public void testToString() {
        assertEquals("8-10 combined tpts", new CombinedTotalPointsRange(8, 10).toString());
    }

    @Test
    public void testAffirmative() {
        assertTrue(new CombinedTotalPointsRange(10, 10).bind(new Players()).test(HandParser.valueOf("AKQ JT9 876 5432")));
        assertTrue(new CombinedTotalPointsRange(10, null).bind(new Players()).test(HandParser.valueOf("AKQ JT9 876 5432")));
        assertFalse(new CombinedTotalPointsRange(11, null).bind(new Players()).test(HandParser.valueOf("AKQ JT9 876 5432")));
        assertTrue(new CombinedTotalPointsRange(null, 10).bind(new Players()).test(HandParser.valueOf("AKQ JT9 876 5432")));
        assertFalse(new CombinedTotalPointsRange(null, 9).bind(new Players()).test(HandParser.valueOf("AKQ JT9 876 5432")));
    }
}
