package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Context;
import bbidder.Hand;
import bbidder.SimpleContext;

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
        Context ctx = new SimpleContext();
        assertTrue(new OpeningPreempt("S", 2).matches(ctx, Hand.valueOf("KQJ234 J 876 543")));
        assertFalse(new OpeningPreempt("S", 3).matches(ctx, Hand.valueOf("KQJ234 J 876 543")));
        assertFalse(new OpeningPreempt("S", 2).matches(ctx, Hand.valueOf("KQJ234 J A76 543")));
    }

    @Test
    public void test3() {
        Context ctx = new SimpleContext();
        assertTrue(new OpeningPreempt("S", 3).matches(ctx, Hand.valueOf("KQJ2345 J 87 543")));
        assertFalse(new OpeningPreempt("S", 4).matches(ctx, Hand.valueOf("KQJ2345 J 87 543")));
        assertFalse(new OpeningPreempt("S", 3).matches(ctx, Hand.valueOf("KQJ2345 J A7 543")));
    }

    @Test
    public void test4() {
        Context ctx = new SimpleContext();
        assertTrue(new OpeningPreempt("S", 4).matches(ctx, Hand.valueOf("KQJ23456 J 87 54")));
        assertFalse(new OpeningPreempt("S", 5).matches(ctx, Hand.valueOf("KQJ23456 J 87 54")));
        assertFalse(new OpeningPreempt("S", 4).matches(ctx, Hand.valueOf("KQJ23456 J A7 54")));
    }

    @Test
    public void test5() {
        Context ctx = new SimpleContext();
        assertTrue(new OpeningPreempt("C", 5).matches(ctx, Hand.valueOf("J 87 5 KQJ234567")));
        assertFalse(new OpeningPreempt("C", 5).matches(ctx, Hand.valueOf("J A7 5 KQJ234567")));
    }
}
