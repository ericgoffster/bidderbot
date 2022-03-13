package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class BidTest {
    @Test
    public void testIsSuitBid() {
        assertTrue(Bid._1C.isSuitBid());

        assertFalse(Bid.P.isSuitBid());
        assertFalse(Bid.X.isSuitBid());
        assertFalse(Bid.XX.isSuitBid());
    }

    @Test
    public void testValueOf() {
        assertEquals(Bid._1C, Bid.valueOf(0, Constants.CLUB));
        assertEquals(Bid._2D, Bid.valueOf(1, Constants.DIAMOND));
        assertEquals(Bid._3H, Bid.valueOf(2, Constants.HEART));
        assertEquals(Bid._4S, Bid.valueOf(3, Constants.SPADE));
        assertEquals(Bid._5N, Bid.valueOf(4, Constants.NOTRUMP));
    }

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
}
