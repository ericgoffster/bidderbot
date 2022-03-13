package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class HandTest {
    @Test
    public void testBasic() {
        Hand hand = Hand.valueOf("AKQJ    T98   765   432");
        assertEquals("AKQJ T98 765 432", hand.toString());
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
}
