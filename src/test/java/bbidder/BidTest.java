package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class BidTest {
    @Test
    public void testFromStr() {
        assertEquals(Bid._1C, Bid.fromStr("1C"));
        assertEquals(Bid._2C, Bid.fromStr("2C"));
        assertEquals(Bid._3C, Bid.fromStr("3C"));
        assertEquals(Bid._4C, Bid.fromStr("4C"));
        assertEquals(Bid._5C, Bid.fromStr("5C"));
        assertEquals(Bid._6C, Bid.fromStr("6C"));
        assertEquals(Bid._7C, Bid.fromStr("7C"));

        assertEquals(Bid._1S, Bid.fromStr("1S"));
        assertEquals(Bid._2S, Bid.fromStr("2S"));
        assertEquals(Bid._3S, Bid.fromStr("3S"));
        assertEquals(Bid._4S, Bid.fromStr("4S"));
        assertEquals(Bid._5S, Bid.fromStr("5S"));
        assertEquals(Bid._6S, Bid.fromStr("6S"));
        assertEquals(Bid._7S, Bid.fromStr("7S"));

        assertEquals(Bid._1S, Bid.fromStr("1S"));
        assertEquals(Bid._2S, Bid.fromStr("2S"));
        assertEquals(Bid._3S, Bid.fromStr("3S"));
        assertEquals(Bid._4S, Bid.fromStr("4S"));
        assertEquals(Bid._5S, Bid.fromStr("5S"));
        assertEquals(Bid._6S, Bid.fromStr("6S"));
        assertEquals(Bid._7S, Bid.fromStr("7S"));

        assertEquals(Bid._1S, Bid.fromStr("1S"));
        assertEquals(Bid._2S, Bid.fromStr("2S"));
        assertEquals(Bid._3S, Bid.fromStr("3S"));
        assertEquals(Bid._4S, Bid.fromStr("4S"));
        assertEquals(Bid._5S, Bid.fromStr("5S"));
        assertEquals(Bid._6S, Bid.fromStr("6S"));
        assertEquals(Bid._7S, Bid.fromStr("7S"));

        assertEquals(Bid._1N, Bid.fromStr("1N"));
        assertEquals(Bid._2N, Bid.fromStr("2N"));
        assertEquals(Bid._3N, Bid.fromStr("3N"));
        assertEquals(Bid._4N, Bid.fromStr("4N"));
        assertEquals(Bid._5N, Bid.fromStr("5N"));
        assertEquals(Bid._6N, Bid.fromStr("6N"));
        assertEquals(Bid._7N, Bid.fromStr("7N"));

        assertEquals(Bid.P, Bid.fromStr("P"));
        assertEquals(Bid.X, Bid.fromStr("X"));
        assertEquals(Bid.XX, Bid.fromStr("XX"));
    }

    @Test
    public void testToString() {
        for (Bid bid : Bid.values()) {
            assertEquals(bid, Bid.fromStr(bid.toString()));
        }
    }

    @Test
    public void testRaise() {
        assertEquals(Bid._2C, Bid._1C.raise());
        assertEquals(Bid._3C, Bid._2C.raise());
        assertEquals(Bid._4C, Bid._3C.raise());
        assertEquals(Bid._5C, Bid._4C.raise());
        assertEquals(Bid._6C, Bid._5C.raise());
        assertEquals(Bid._7C, Bid._6C.raise());

        assertEquals(Bid._2S, Bid._1S.raise());
        assertEquals(Bid._3S, Bid._2S.raise());
        assertEquals(Bid._4S, Bid._3S.raise());
        assertEquals(Bid._5S, Bid._4S.raise());
        assertEquals(Bid._6S, Bid._5S.raise());
        assertEquals(Bid._7S, Bid._6S.raise());

        assertEquals(Bid._2S, Bid._1S.raise());
        assertEquals(Bid._3S, Bid._2S.raise());
        assertEquals(Bid._4S, Bid._3S.raise());
        assertEquals(Bid._5S, Bid._4S.raise());
        assertEquals(Bid._6S, Bid._5S.raise());
        assertEquals(Bid._7S, Bid._6S.raise());

        assertEquals(Bid._2S, Bid._1S.raise());
        assertEquals(Bid._3S, Bid._2S.raise());
        assertEquals(Bid._4S, Bid._3S.raise());
        assertEquals(Bid._5S, Bid._4S.raise());
        assertEquals(Bid._6S, Bid._5S.raise());
        assertEquals(Bid._7S, Bid._6S.raise());

        assertEquals(Bid._2N, Bid._1N.raise());
        assertEquals(Bid._3N, Bid._2N.raise());
        assertEquals(Bid._4N, Bid._3N.raise());
        assertEquals(Bid._5N, Bid._4N.raise());
        assertEquals(Bid._6N, Bid._5N.raise());
        assertEquals(Bid._7N, Bid._6N.raise());

    }

    @Test
    public void testIsMajor() {
        assertFalse(Bid._1C.isMajor());
        assertFalse(Bid._2C.isMajor());
        assertFalse(Bid._3C.isMajor());
        assertFalse(Bid._4C.isMajor());
        assertFalse(Bid._5C.isMajor());
        assertFalse(Bid._6C.isMajor());
        assertFalse(Bid._7C.isMajor());

        assertFalse(Bid._1D.isMajor());
        assertFalse(Bid._2D.isMajor());
        assertFalse(Bid._3D.isMajor());
        assertFalse(Bid._4D.isMajor());
        assertFalse(Bid._5D.isMajor());
        assertFalse(Bid._6D.isMajor());
        assertFalse(Bid._7D.isMajor());

        assertTrue(Bid._1H.isMajor());
        assertTrue(Bid._2H.isMajor());
        assertTrue(Bid._3H.isMajor());
        assertTrue(Bid._4H.isMajor());
        assertTrue(Bid._5H.isMajor());
        assertTrue(Bid._6H.isMajor());
        assertTrue(Bid._7H.isMajor());

        assertTrue(Bid._1S.isMajor());
        assertTrue(Bid._2S.isMajor());
        assertTrue(Bid._3S.isMajor());
        assertTrue(Bid._4S.isMajor());
        assertTrue(Bid._5S.isMajor());
        assertTrue(Bid._6S.isMajor());
        assertTrue(Bid._7S.isMajor());

        assertFalse(Bid._1N.isMajor());
        assertFalse(Bid._2N.isMajor());
        assertFalse(Bid._3N.isMajor());
        assertFalse(Bid._4N.isMajor());
        assertFalse(Bid._5N.isMajor());
        assertFalse(Bid._6N.isMajor());
        assertFalse(Bid._7N.isMajor());
    }

    @Test
    public void testIsMinor() {
        assertTrue(Bid._1C.isMinor());
        assertTrue(Bid._2C.isMinor());
        assertTrue(Bid._3C.isMinor());
        assertTrue(Bid._4C.isMinor());
        assertTrue(Bid._5C.isMinor());
        assertTrue(Bid._6C.isMinor());
        assertTrue(Bid._7C.isMinor());

        assertTrue(Bid._1D.isMinor());
        assertTrue(Bid._2D.isMinor());
        assertTrue(Bid._3D.isMinor());
        assertTrue(Bid._4D.isMinor());
        assertTrue(Bid._5D.isMinor());
        assertTrue(Bid._6D.isMinor());
        assertTrue(Bid._7D.isMinor());

        assertFalse(Bid._1H.isMinor());
        assertFalse(Bid._2H.isMinor());
        assertFalse(Bid._3H.isMinor());
        assertFalse(Bid._4H.isMinor());
        assertFalse(Bid._5H.isMinor());
        assertFalse(Bid._6H.isMinor());
        assertFalse(Bid._7H.isMinor());

        assertFalse(Bid._1S.isMinor());
        assertFalse(Bid._2S.isMinor());
        assertFalse(Bid._3S.isMinor());
        assertFalse(Bid._4S.isMinor());
        assertFalse(Bid._5S.isMinor());
        assertFalse(Bid._6S.isMinor());
        assertFalse(Bid._7S.isMinor());

        assertFalse(Bid._1N.isMinor());
        assertFalse(Bid._2N.isMinor());
        assertFalse(Bid._3N.isMinor());
        assertFalse(Bid._4N.isMinor());
        assertFalse(Bid._5N.isMinor());
        assertFalse(Bid._6N.isMinor());
        assertFalse(Bid._7N.isMinor());
    }
}
