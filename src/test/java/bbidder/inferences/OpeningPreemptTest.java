package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Hand;
import bbidder.InferenceContext;
import bbidder.Players;

public class OpeningPreemptTest {
    @Test
    public void testValueOf() {
        assertEquals(new OpeningPreempt("S", 2), OpeningPreempt.valueOf("opening_preempt 2 S"));
        assertEquals(new OpeningPreempt("S", 2), OpeningPreempt.valueOf("   opening_preempt    2   S   "));
    }

    @Test
    public void testToString() {
        assertEquals("opening_preempt 2 S", new OpeningPreempt("S", 2).toString());
    }

    @Test
    public void test2() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new OpeningPreempt("S", 2).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("KQJ234 J 876 543")));
        assertFalse(new OpeningPreempt("S", 3).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("KQJ234 J 876 543")));
        assertFalse(new OpeningPreempt("S", 2).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("KQJ234 J A76 543")));
    }

    @Test
    public void test3() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new OpeningPreempt("S", 3).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("KQJ2345 J 87 543")));
        assertFalse(new OpeningPreempt("S", 4).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("KQJ2345 J 87 543")));
        assertFalse(new OpeningPreempt("S", 3).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("KQJ2345 J A7 543")));
    }

    @Test
    public void test4() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new OpeningPreempt("S", 4).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("KQJ23456 J 87 54")));
        assertFalse(new OpeningPreempt("S", 5).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("KQJ23456 J 87 54")));
        assertFalse(new OpeningPreempt("S", 4).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("KQJ23456 J A7 54")));
    }

    @Test
    public void test5() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(new OpeningPreempt("C", 5).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("J 87 5 KQJ234567")));
        assertFalse(new OpeningPreempt("C", 5).bind(ctx).get(0).inf.matches(new Players(), Hand.valueOf("J A7 5 KQJ234567")));
    }
}
