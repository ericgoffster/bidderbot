package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RangeTest {
    @Test
    public void testAll() {
        Range r = Range.all(5);
        for (int i = 0; i <= 5; i++) {
            assertTrue(r.contains(i));
        }
        assertFalse(r.isEmpty());
        assertTrue(r.unBounded());
        assertThrows(IllegalArgumentException.class, () -> r.contains(6));
    }

    @Test
    public void testExtremes() {
        Range r = Range.between(2, 4, 5);
        assertEquals(r.highest().get().intValue(), 4);
        assertEquals(r.lowest().get().intValue(), 2);

        assertEquals(Range.atLeast(null, 5), Range.all(5));
        assertThrows(IllegalArgumentException.class, () -> Range.atLeast(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> Range.atLeast(6, 5));

        assertEquals(Range.atMost(null, 5), Range.all(5));
        assertThrows(IllegalArgumentException.class, () -> Range.atMost(6, 5));
        assertThrows(IllegalArgumentException.class, () -> Range.atMost(-1, 5));
    }

    @Test
    public void testNone() {
        Range r = Range.none(5);
        for (int i = 0; i <= 5; i++) {
            assertFalse(r.contains(i));
        }
        assertTrue(r.isEmpty());
        assertFalse(r.unBounded());
        assertThrows(IllegalArgumentException.class, () -> r.contains(6));
    }

    @Test
    public void testDisJoint() {
        Range r = Range.atMost(1, 5).or(Range.atLeast(4, 5));
        assertTrue(r.contains(0));
        assertTrue(r.contains(1));
        assertFalse(r.contains(2));
        assertFalse(r.contains(3));
        assertTrue(r.contains(4));
        assertTrue(r.contains(5));
        assertFalse(r.isEmpty());
        assertFalse(r.unBounded());
        assertThrows(IllegalArgumentException.class, () -> r.contains(6));
    }

    @Test
    public void testAdd() {
        Range r = Range.none(5).add(1).add(2);
        assertEquals(r, Range.between(1, 2, 5));
    }

    @Test
    public void testNot() {
        Range r = Range.atMost(1, 5).or(Range.atLeast(4, 5));
        assertEquals(r.not(), Range.between(2, 3, 5));
    }

    @Test
    public void testAnd() {
        Range r = Range.atMost(4, 5).and(Range.atLeast(3, 5));
        assertEquals(r, Range.between(3, 4, 5));
    }

    @Test
    public void testOr() {
        Range r = Range.between(2, 4, 5).or(Range.between(3, 5, 5));
        assertEquals(r, Range.between(2, 5, 5));
    }
}
