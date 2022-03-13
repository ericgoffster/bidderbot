package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class HandTest {
    @Test
    public void testBasic() {
        Hand hand = Hand.valueOf("AKQJ    T98   765   432");
        assertEquals("AKQJ T98 765 432", hand.toString());
        assertEquals(hand.getShape(), Shape._03030304);
        assertTrue(hand.getAllInSuit(0) == 0x7);
        assertTrue(hand.getAllInSuit(1) >> 3 == 0x7);
        assertTrue(hand.getAllInSuit(2) >> 6 == 0x7);
        assertTrue(hand.getAllInSuit(3) >> 9 == 0xf);
    }

    @Test
    public void testEmpty() {
        Hand hand = Hand.valueOf("    AKQJ  -  765    432");
        assertEquals("AKQJ - 765 432", hand.toString());
    }

    @Test
    public void getHCP() {
        Hand hand = Hand.valueOf("    AKQJ  -  765    432");
        assertEquals(10, hand.numHCP());
    }

    @Test
    public void testTotalPts() {
        Hand hand = Hand.valueOf("    AKQJ  -  T7658    9432");
        assertEquals(13, hand.totalPoints(0));
        assertEquals(10, hand.totalPoints(2));
    }

    @Test
    public void testGetShape() {
        Hand hand = Hand.valueOf("    AKQJ  -  T7658    9432");
        assertEquals(hand.getShape(), Shape._04050004);
    }

    @Test
    public void testFit() {
        Hand hand = Hand.valueOf("    AKQJ  xxx  xx    9432");
        assertFalse(hand.haveFit(InfSummary.ALL, 0));
        assertTrue(hand.haveFit(new InfSummary(new ShapeSet(shape -> shape.numInSuit(0) >= 4), Range.all(40), StopperSet.ALL), 0));
        assertFalse(hand.haveFit(new InfSummary(new ShapeSet(shape -> shape.numInSuit(0) >= 4), Range.all(40), StopperSet.ALL), 1));
        assertFalse(hand.haveFit(new InfSummary(new ShapeSet(shape -> shape.numInSuit(0) >= 3), Range.all(40), StopperSet.ALL), 0));
    }

    @Test
    public void testStoppers() {
        assertFalse(Hand.valueOf("A Kxxxx Qx xxxxx").haveStopper(0));
        assertFalse(Hand.valueOf("A Kxxxx Qx xxxxx").haveStopper(1));
        assertTrue(Hand.valueOf("A Kxxxx Qx xxxxx").haveStopper(2));
        assertTrue(Hand.valueOf("A Kxxxx Qx xxxxx").haveStopper(3));
    }
}
