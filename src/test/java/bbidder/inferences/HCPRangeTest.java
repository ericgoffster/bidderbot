package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Context;
import bbidder.Hand;
import bbidder.SimpleContext;

public class HCPRangeTest {
    @Test
    public void testValueOf() {
        assertEquals(new HCPRange("10", null), HCPRange.valueOf("10+hcp"));
        assertEquals(new HCPRange("10", null), HCPRange.valueOf("  10 +  Hcp    "));
        assertEquals(new HCPRange(null, "10"), HCPRange.valueOf("10-hcp"));
        assertEquals(new HCPRange(null, "10"), HCPRange.valueOf("  10 -  Hcp    "));
        assertEquals(new HCPRange("8", "10"), HCPRange.valueOf("8-10hcp"));
        assertEquals(new HCPRange("8", "10"), HCPRange.valueOf("  8 - 10    Hcp    "));
        assertEquals(new HCPRange("8", "8"), HCPRange.valueOf("  8    Hcp    "));
    }

    @Test
    public void testToString() {
        assertEquals("8-10 hcp", new HCPRange("8", "10").toString());
        assertEquals("8+ hcp", new HCPRange("8", null).toString());
        assertEquals("10- hcp", new HCPRange(null, "10").toString());
        assertEquals("10 hcp", new HCPRange("10", "10").toString());
    }

    @Test
    public void test() {
        Context ctx = new SimpleContext();
        assertTrue(new HCPRange("10", "10").matches(ctx, Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new HCPRange("10", null).matches(ctx, Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new HCPRange("11", null).matches(ctx, Hand.valueOf("AKQ JT9 876 5432")));
        assertTrue(new HCPRange(null, "10").matches(ctx, Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(new HCPRange(null, "9").matches(ctx, Hand.valueOf("AKQ JT9 876 5432")));
    }
}
