package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.parsers.HandParser;

public class HandTest {
    @Test
    public void testBasic() {
        Hand hand = HandParser.valueOf("AKQJ    T98   765   432");
        assertEquals("AKQJ T98 765 432", hand.toString());
        assertEquals(hand.getShape(), Shape._03030304);
        assertTrue(hand.getAllInSuit(0) == 0x7);
        assertTrue(hand.getAllInSuit(1) >> 3 == 0x7);
        assertTrue(hand.getAllInSuit(2) >> 6 == 0x7);
        assertTrue(hand.getAllInSuit(3) >> 9 == 0xf);
    }

    @Test
    public void testEmpty() {
        Hand hand = HandParser.valueOf("    AKQJ  -  765    432");
        assertEquals("AKQJ - 765 432", hand.toString());
    }

    @Test
    public void getHCP() {
        Hand hand = HandParser.valueOf("    AKQJ  -  765    432");
        assertEquals(10, hand.numHCP());
    }

    @Test
    public void testTotalPts() {
        Hand hand = HandParser.valueOf("    AKQJ  -  T7658    9432");
        assertEquals(13, hand.totalPoints(0));
        assertEquals(10, hand.totalPoints(2));
    }

    @Test
    public void testGetShape() {
        Hand hand = HandParser.valueOf("    AKQJ  -  T7658    9432");
        assertEquals(hand.getShape(), Shape._04050004);
    }

    @Test
    public void testFit() {
        Hand hand = HandParser.valueOf("    AKQJ  xxx  xx    9432");
        assertFalse(hand.haveFit(InfSummary.ALL, 0));
        assertTrue(hand.haveFit(InfSummary.ALL.withShapes(ShapeSet.create(shape -> shape.numInSuit(0) >= 4)), 0));
        assertFalse(hand.haveFit(InfSummary.ALL.withShapes(ShapeSet.create(shape -> shape.numInSuit(0) >= 4)), 1));
        assertFalse(hand.haveFit(InfSummary.ALL.withShapes(ShapeSet.create(shape -> shape.numInSuit(0) >= 3)), 0));
    }

    @Test
    public void testStoppers() {
        assertEquals(HandParser.valueOf("A Kxxxx Qx xxxxx").getStoppers(), Stoppers.EMPTY.withStopperIn(3).withStopperIn(2));
        assertEquals(HandParser.valueOf("A Kxxxx Qx xxxxx").getPartialStoppers(), Stoppers.EMPTY.withStopperIn(1).withStopperIn(3).withStopperIn(2));
        assertEquals(HandParser.valueOf("A Kxxxx Qx xxxxx").getPartialStoppers(), Stoppers.EMPTY.withStopperIn(1).withStopperIn(3).withStopperIn(2));
    }
}
