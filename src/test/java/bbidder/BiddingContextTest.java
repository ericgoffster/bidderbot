package bbidder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Set;

import org.junit.Test;

public class BiddingContextTest {
    @Test
    public void testmc() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1C, BidPattern.valueOf("1m"));
        assertNull(bc.getSuit("M"));
        assertNull(bc.getSuit("OM"));
        assertEquals(bc.getSuit("m").intValue(), 0);
        assertEquals(bc.getSuit("om").intValue(), 1);
    }

    @Test
    public void testomc() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1C, BidPattern.valueOf("1om"));
        assertNull(bc.getSuit("M"));
        assertNull(bc.getSuit("OM"));
        assertEquals(bc.getSuit("m").intValue(), 1);
        assertEquals(bc.getSuit("om").intValue(), 0);
    }

    @Test
    public void testmd() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1D, BidPattern.valueOf("1m"));
        assertNull(bc.getSuit("M"));
        assertNull(bc.getSuit("OM"));
        assertEquals(bc.getSuit("m").intValue(), 1);
        assertEquals(bc.getSuit("om").intValue(), 0);
    }

    @Test
    public void testomd() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1D, BidPattern.valueOf("1om"));
        assertNull(bc.getSuit("M"));
        assertNull(bc.getSuit("OM"));
        assertEquals(bc.getSuit("m").intValue(), 0);
        assertEquals(bc.getSuit("om").intValue(), 1);
    }

    @Test
    public void testMH() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1H, BidPattern.valueOf("1M"));
        assertNull(bc.getSuit("m"));
        assertNull(bc.getSuit("om"));
        assertEquals(bc.getSuit("M").intValue(), 2);
        assertEquals(bc.getSuit("OM").intValue(), 3);
    }

    @Test
    public void testOMH() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1H, BidPattern.valueOf("1OM"));
        assertNull(bc.getSuit("m"));
        assertNull(bc.getSuit("om"));
        assertEquals(bc.getSuit("M").intValue(), 3);
        assertEquals(bc.getSuit("OM").intValue(), 2);
    }

    @Test
    public void testMS() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1S, BidPattern.valueOf("1M"));
        assertNull(bc.getSuit("m"));
        assertNull(bc.getSuit("om"));
        assertEquals(bc.getSuit("M").intValue(), 3);
        assertEquals(bc.getSuit("OM").intValue(), 2);
    }

    @Test
    public void testOMS() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1S, BidPattern.valueOf("1OM"));
        assertNull(bc.getSuit("m"));
        assertNull(bc.getSuit("om"));
        assertEquals(bc.getSuit("M").intValue(), 2);
        assertEquals(bc.getSuit("OM").intValue(), 3);
    }

    @Test
    public void testx() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1C, BidPattern.valueOf("1x"));
        assertNull(bc.getSuit("M"));
        assertNull(bc.getSuit("OM"));
        assertNull(bc.getSuit("m"));
        assertNull(bc.getSuit("om"));
        assertEquals(bc.getSuit("x").intValue(), 0);
    }

    @Test
    public void testxm1() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1D, BidPattern.valueOf("1x-1"));
        assertEquals(bc.getSuit("x").intValue(), 2);
    }

    @Test
    public void testxm2() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1D, BidPattern.valueOf("1x-2"));
        assertEquals(bc.getSuit("x").intValue(), 3);
    }

    @Test
    public void testxm3() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1H, BidPattern.valueOf("1x-3"));
        assertEquals(bc.getSuit("x").intValue(), 0);
    }

    @Test
    public void testDown() {
        BiddingContext bc = BiddingContext.EMPTY;
        assertArrayEquals(bc.getBids(BidPattern.valueOf("1M:down")).keySet().toArray(new Bid[0]), new Bid[] { Bid._1S, Bid._1H });
        assertArrayEquals(bc.getBids(BidPattern.valueOf("1M")).keySet().toArray(new Bid[0]), new Bid[] { Bid._1H, Bid._1S });
    }

    @Test
    public void testReverse() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1D, BidPattern.valueOf("1D"))
                .withNewBid(Bid.P, BidPattern.valueOf("P"))
                .withNewBid(Bid._1S, BidPattern.valueOf("1S"))
                .withNewBid(Bid.P, BidPattern.valueOf("P"));
        assertEquals(bc.getBids(BidPattern.valueOf("RVx")).keySet(), Set.of(Bid._2H));
        assertEquals(bc.getBids(BidPattern.valueOf("NRx")).keySet(), Set.of(Bid._2C));
    }

    @Test
    public void testReverse11() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1C, BidPattern.valueOf("1C"))
                .withNewBid(Bid.P, BidPattern.valueOf("P"))
                .withNewBid(Bid._1D, BidPattern.valueOf("1D"))
                .withNewBid(Bid.P, BidPattern.valueOf("P"));
        assertEquals(bc.getBids(BidPattern.valueOf("RVx")).keySet(), Set.of());
        assertEquals(bc.getBids(BidPattern.valueOf("NRx")).keySet(), Set.of());
    }

    @Test
    public void testHighReverse() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1S, BidPattern.valueOf("1S"))
                .withNewBid(Bid.P, BidPattern.valueOf("P"))
                .withNewBid(Bid._2H, BidPattern.valueOf("2H"))
                .withNewBid(Bid.P, BidPattern.valueOf("P"));
        assertEquals(bc.getBids(BidPattern.valueOf("RVx")).keySet(), Set.of(Bid._3C, Bid._3D));
        assertEquals(bc.getBids(BidPattern.valueOf("NRx")).keySet(), Set.of());
    }

    @Test
    public void testCombo() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1C, BidPattern.valueOf("1x")).withNewBid(Bid._1H, BidPattern.valueOf("1M"));
        assertEquals(bc.getSuit("M").intValue(), 2);
        assertEquals(bc.getSuit("x").intValue(), 0);
    }

    @Test
    public void testGetBids() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1C, BidPattern.valueOf("1x"));
        assertEquals(bc.getBids(BidPattern.valueOf("1y")).keySet(), Set.of(Bid._1D, Bid._1H, Bid._1S));
    }

    @Test
    public void testGetBids2() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1C, BidPattern.valueOf("1x"));
        assertEquals(bc.getBids(BidPattern.valueOf("1M")).keySet(), Set.of(Bid._1H, Bid._1S));
    }

    @Test
    public void testGetBids3() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1C, BidPattern.valueOf("1x"));
        assertEquals(bc.getBids(BidPattern.valueOf("1m")).keySet(), Set.of(Bid._1D));
    }

    @Test
    public void testGetBids4() {
        BiddingContext bc = BiddingContext.EMPTY.withNewBid(Bid._1H, BidPattern.valueOf("1x"));
        assertEquals(bc.getBids(BidPattern.valueOf("NJy")).keySet(), Set.of(Bid._2C, Bid._2D, Bid._1S));
        assertEquals(bc.getBids(BidPattern.valueOf("Jy")).keySet(), Set.of(Bid._3C, Bid._3D, Bid._2S));
        assertEquals(bc.getBids(BidPattern.valueOf("DJy")).keySet(), Set.of(Bid._4C, Bid._4D, Bid._3S));
    }

    @Test
    public void testInvalid() {
        BiddingContext bc = BiddingContext.EMPTY;
        assertThrows(IllegalArgumentException.class, () -> bc.withNewBid(Bid._1C, BidPattern.valueOf("1M")));
        assertThrows(IllegalArgumentException.class, () -> bc.withNewBid(Bid._1C, BidPattern.valueOf("1OM")));
        assertThrows(IllegalArgumentException.class, () -> bc.withNewBid(Bid._1H, BidPattern.valueOf("1m")));
        assertThrows(IllegalArgumentException.class, () -> bc.withNewBid(Bid._1H, BidPattern.valueOf("1om")));
        assertThrows(IllegalArgumentException.class, () -> bc.withNewBid(Bid._1N, BidPattern.valueOf("1x")));
        assertThrows(IllegalArgumentException.class, () -> bc.withNewBid(Bid.P, BidPattern.valueOf("1x")));
    }
}
