package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.junit.Test;

public class BiddingContextTest {
    @Test
    public void testmc() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1C, BidPattern.valueOf("1m"));
        assertNull(bc.getSuit("M"));
        assertNull(bc.getSuit("OM"));
        assertEquals(bc.getSuit("m").intValue(), 0);
        assertEquals(bc.getSuit("om").intValue(), 1);
    }
    @Test
    public void testomc() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1C, BidPattern.valueOf("1om"));
        assertNull(bc.getSuit("M"));
        assertNull(bc.getSuit("OM"));
        assertEquals(bc.getSuit("m").intValue(), 1);
        assertEquals(bc.getSuit("om").intValue(), 0);
    }
    @Test
    public void testmd() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1D, BidPattern.valueOf("1m"));
        assertNull(bc.getSuit("M"));
        assertNull(bc.getSuit("OM"));
        assertEquals(bc.getSuit("m").intValue(), 1);
        assertEquals(bc.getSuit("om").intValue(), 0);
    }
    @Test
    public void testomd() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1D, BidPattern.valueOf("1om"));
        assertNull(bc.getSuit("M"));
        assertNull(bc.getSuit("OM"));
        assertEquals(bc.getSuit("m").intValue(), 0);
        assertEquals(bc.getSuit("om").intValue(), 1);
    }
    @Test
    public void testMH() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1H, BidPattern.valueOf("1M"));
        assertNull(bc.getSuit("m"));
        assertNull(bc.getSuit("om"));
        assertEquals(bc.getSuit("M").intValue(), 2);
        assertEquals(bc.getSuit("OM").intValue(), 3);
    }
    @Test
    public void testOMH() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1H, BidPattern.valueOf("1OM"));
        assertNull(bc.getSuit("m"));
        assertNull(bc.getSuit("om"));
        assertEquals(bc.getSuit("M").intValue(), 3);
        assertEquals(bc.getSuit("OM").intValue(), 2);
    }
    @Test
    public void testMS() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1S, BidPattern.valueOf("1M"));
        assertNull(bc.getSuit("m"));
        assertNull(bc.getSuit("om"));
        assertEquals(bc.getSuit("M").intValue(), 3);
        assertEquals(bc.getSuit("OM").intValue(), 2);
    }
    @Test
    public void testOMS() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1S, BidPattern.valueOf("1OM"));
        assertNull(bc.getSuit("m"));
        assertNull(bc.getSuit("om"));
        assertEquals(bc.getSuit("M").intValue(), 2);
        assertEquals(bc.getSuit("OM").intValue(), 3);
    }
    @Test
    public void testx() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1C, BidPattern.valueOf("1x"));
        assertNull(bc.getSuit("M"));
        assertNull(bc.getSuit("OM"));
        assertNull(bc.getSuit("m"));
        assertNull(bc.getSuit("om"));
        assertEquals(bc.getSuit("x").intValue(), 0);
    }
    @Test
    public void testCombo() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1C, BidPattern.valueOf("1x"));
        bc = bc.withNewBid(Bid._1H, BidPattern.valueOf("1M"));
        assertEquals(bc.getSuit("M").intValue(), 2);
        assertEquals(bc.getSuit("x").intValue(), 0);
    }
    @Test
    public void testGetBids() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1C, BidPattern.valueOf("1x"));
        assertEquals(bc.getBids(BidPattern.valueOf("1y")), Set.of(Bid._1D, Bid._1H, Bid._1S));
    }
    @Test
    public void testGetBids2() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1C, BidPattern.valueOf("1x"));
        assertEquals(bc.getBids(BidPattern.valueOf("1M")), Set.of(Bid._1H, Bid._1S));
    }
    @Test
    public void testGetBids3() {
        BiddingContext bc = new BiddingContext();
        bc = bc.withNewBid(Bid._1C, BidPattern.valueOf("1x"));
        assertEquals(bc.getBids(BidPattern.valueOf("1m")), Set.of(Bid._1D));
    }
}