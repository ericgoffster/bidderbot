package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.IBoundInference;
import bbidder.Players;
import bbidder.parsers.HandParser;
import bbidder.parsers.InferenceParser;

public class BalancedTest {
    @Test
    public void testValueOf() {
        assertEquals(Balanced.BALANCED, InferenceParser.parseInference("balanced"));
    }

    @Test
    public void testToString() {
        assertEquals("balanced", Balanced.BALANCED.toString());
    }

    @Test
    public void testAffirmative() {
        IBoundInference inf = Balanced.BALANCED.bind(new Players());
        assertTrue(inf.test(HandParser.parseHand("AKQ JT9 876 5432")));
        assertTrue(inf.test(HandParser.parseHand("AKQJT 98 765 432")));
        assertFalse(inf.test(HandParser.parseHand("AKQJT 98 76 5432")));
        assertFalse(inf.test(HandParser.parseHand("AKQJT 9 876 5432")));
        assertFalse(inf.test(HandParser.parseHand("AKQJT - 9876 5432")));
    }
}
