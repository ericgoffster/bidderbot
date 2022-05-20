package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.InferenceParser;
import bbidder.Players;
import bbidder.Symbol;
import bbidder.symbols.ConstSymbol;

public class SuitRangeTest {
    @Test
    public void testValueOf() {
        Symbol S = new ConstSymbol(3);
        assertEquals(new SuitRange(S, 10, null), InferenceParser.parseInference("10+ s"));
    }

    @Test
    public void testToString() {
        Symbol S = new ConstSymbol(3);
        assertEquals("8-10 S", new SuitRange(S, 8, 10).toString());
    }

    @Test
    public void test() {
        Symbol S = new ConstSymbol(3);
        assertTrue(new SuitRange(S, 3, 3).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange(S, 3, 4).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange(S, null, 3).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new SuitRange(S, 4, null).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        Symbol C = new ConstSymbol(0);
        assertFalse(new SuitRange(C, 3, 3).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange(C, 3, 4).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new SuitRange(C, 2, 3).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new SuitRange(C, 4, null).bind(new Players()).test(Hand.valueOf("AKQ JT9 876 5432")));
    }
}
