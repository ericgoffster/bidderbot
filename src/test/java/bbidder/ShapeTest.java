package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ShapeTest {
    @Test
    public void testBalanced() {
        assertTrue(Shape._04030303.isBalanced());
        assertTrue(Shape._04030303.isSuperBalanced());
        assertTrue(Shape._05030302.isBalanced());
        assertFalse(Shape._05030302.isSuperBalanced());
        assertFalse(Shape._05040202.isBalanced());
        assertFalse(Shape._05040301.isBalanced());
    }

    @Test
    public void testLongestOrEquals() {
        assertTrue(Shape._04040302.isLongerOrEqual(1, Constants.MINORS));
        assertTrue(Shape._04040302.isLongerOrEqual(0, Constants.MINORS));
        assertFalse(Shape._03040402.isLongerOrEqual(0, Constants.MINORS));
        assertTrue(Shape._03040402.isLongerOrEqual(1, Constants.MINORS));
    }

    @Test
    public void testNumInSuit() {
        assertEquals(Shape._04040302.numInSuit(0), 4);
        assertEquals(Shape._04040302.numInSuit(1), 4);
        assertEquals(Shape._04040302.numInSuit(2), 3);
        assertEquals(Shape._04040302.numInSuit(3), 2);
    }

    @Test
    public void testSuitInRange() {
        assertTrue(Shape._04040302.isSuitInRange(0, SuitLengthRange.between(3, 5)));
        assertFalse(Shape._04040302.isSuitInRange(0, SuitLengthRange.between(5, 6)));
    }

    @Test
    public void testGetShape() {
        assertEquals(Shape._04040302, Shape.getShape(4, 4, 3, 2));
    }
}
