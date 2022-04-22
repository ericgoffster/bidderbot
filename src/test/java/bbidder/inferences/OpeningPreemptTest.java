package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.Players;
import bbidder.Symbol;
import bbidder.symbols.BoundSymbol;
import bbidder.symbols.ConstSymbol;

public class OpeningPreemptTest {
    @Test
    public void testValueOf() {
        Symbol S = new ConstSymbol(3);
        assertEquals(new OpeningPreempt(S, 2), OpeningPreempt.valueOf("opening_preempt 2 S"));
        assertEquals(new OpeningPreempt(S, 2), OpeningPreempt.valueOf("   opening_preempt    2   S   "));
    }

    @Test
    public void testToString() {
        Symbol S = new ConstSymbol(3);
        assertEquals("opening_preempt 2 S", new OpeningPreempt(S, 2).toString());
    }

    @Test
    public void test2() {
        Symbol S = new BoundSymbol(3, new ConstSymbol(3));
        assertTrue(new OpeningPreempt(S, 2).bind(new Players()).matches(Hand.valueOf("KQJ234 J 876 543")));
        assertFalse(new OpeningPreempt(S, 3).bind(new Players()).matches(Hand.valueOf("KQJ234 J 876 543")));
        assertFalse(new OpeningPreempt(S, 2).bind(new Players()).matches(Hand.valueOf("KQJ234 J A76 543")));
    }

    @Test
    public void test3() {
        Symbol S = new BoundSymbol(3, new ConstSymbol(3));
        assertTrue(new OpeningPreempt(S, 3).bind(new Players()).matches(Hand.valueOf("KQJ2345 J 87 543")));
        assertFalse(new OpeningPreempt(S, 4).bind(new Players()).matches(Hand.valueOf("KQJ2345 J 87 543")));
        assertFalse(new OpeningPreempt(S, 3).bind(new Players()).matches(Hand.valueOf("KQJ2345 J A7 543")));
    }

    @Test
    public void test4() {
        Symbol S = new BoundSymbol(3, new ConstSymbol(3));
        assertTrue(new OpeningPreempt(S, 4).bind(new Players()).matches(Hand.valueOf("KQJ23456 J 87 54")));
        assertFalse(new OpeningPreempt(S, 5).bind(new Players()).matches(Hand.valueOf("KQJ23456 J 87 54")));
        assertFalse(new OpeningPreempt(S, 4).bind(new Players()).matches(Hand.valueOf("KQJ23456 J A7 54")));
    }

    @Test
    public void test5() {
        Symbol C = new BoundSymbol(0, new ConstSymbol(0));
        assertTrue(new OpeningPreempt(C, 5).bind(new Players()).matches(Hand.valueOf("J 87 5 KQJ234567")));
        assertFalse(new OpeningPreempt(C, 5).bind(new Players()).matches(Hand.valueOf("J A7 5 KQJ234567")));
    }
}
