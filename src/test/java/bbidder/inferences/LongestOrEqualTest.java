package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitSetParser;
import bbidder.Symbol;
import bbidder.symbols.ConstSymbol;
import bbidder.symbols.VarSymbol;

public class LongestOrEqualTest {
    SuitSet ALL = SuitSetParser.lookupSuitSet("ALL");

    @Test
    public void testValueOf() {
        Symbol sym = new ConstSymbol(3);
        assertEquals(new LongestOrEqual(sym, ALL), LongestOrEqual.valueOf("longest_or_equal s among all"));
        assertEquals(new LongestOrEqual(sym, ALL), LongestOrEqual.valueOf("   longest_or_equal    s    among    all    "));
        assertEquals(new LongestOrEqual(sym, null), LongestOrEqual.valueOf("longest_or_equal s"));
        assertEquals(new LongestOrEqual(sym, null), LongestOrEqual.valueOf("    longest_or_equal  s    "));
    }

    @Test
    public void testToString() {
        assertEquals("longest_or_equal x among ALL", new LongestOrEqual(new VarSymbol("x"), ALL).toString());
        assertEquals("longest_or_equal s", new LongestOrEqual(new VarSymbol("s"), null).toString());
    }

    @Test
    public void testHigherRanking() {
        assertTrue(new LongestOrEqual(new ConstSymbol(3), ALL).bind(new Players()).test(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertTrue(new LongestOrEqual(new ConstSymbol(2), ALL).bind(new Players()).test(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual(new ConstSymbol(1), ALL).bind(new Players()).test(Hand.valueOf("AKQJ AKQJ 765 43")));
        assertFalse(new LongestOrEqual(new ConstSymbol(0), ALL).bind(new Players()).test(Hand.valueOf("AKQJ AKQJ 765 43")));
    }
}
