package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Test;

public class BidListTest {
    @Test
    public void testExceptLast() {
        assertEquals(Auction.valueOf("1C (1D) 1H (X) XX").exceptLast(), Auction.valueOf("1C (1D) 1H (X)"));
    }

    @Test
    public void testGetContract() {
        assertEquals(Auction.valueOf("1C (1D) 1H (X) XX").getContract(), new Contract(2, Bid._1H, true, true));
        assertEquals(Auction.valueOf("1C (1D) 1H (X)").getContract(), new Contract(2, Bid._1H, true, false));
        assertEquals(Auction.valueOf("1C (1D) 1H").getContract(), new Contract(2, Bid._1H, false, false));
        assertEquals(Auction.valueOf("1C (1D) 1H P").getContract(), new Contract(2, Bid._1H, false, false));
    }

    @Test
    public void testGetLastBid() {
        assertEquals(Auction.valueOf("1C (1D) 1H (X) XX").getLastBid(), Bid.XX);
        assertEquals(Auction.valueOf("1C (1D) 1H (X) XX").getLastSuitBid(), Bid._1H);
        assertNull(Auction.valueOf("P").getLastSuitBid());
    }

    @Test
    public void testValueOf() {
        assertEquals(Auction.valueOf("P"), Auction.create(List.of(Bid.P)));
        assertEquals(Auction.valueOf("P P"), Auction.create(List.of(Bid.P, Bid.P, Bid.P)));
        assertEquals(Auction.valueOf("P (P)"), Auction.create(List.of(Bid.P, Bid.P)));
        assertEquals(Auction.valueOf("1C"), Auction.create(List.of(Bid._1C)));
        assertEquals(Auction.valueOf("1C 1D"), Auction.create(List.of(Bid._1C, Bid.P, Bid._1D)));
        assertEquals(Auction.valueOf("1C (1D) 1H"), Auction.create(List.of(Bid._1C, Bid._1D, Bid._1H)));
        assertEquals(Auction.valueOf("1C (1D) 1H (X)"), Auction.create(List.of(Bid._1C, Bid._1D, Bid._1H, Bid.X)));
        assertEquals(Auction.valueOf("1C (1D) 1H (X) XX"), Auction.create(List.of(Bid._1C, Bid._1D, Bid._1H, Bid.X, Bid.XX)));
    }

    @Test
    public void testLegailty() {
        assertEquals(Auction.valueOf("P").getContract(), new Contract(0, Bid.P, false, false));
        assertEquals(Auction.valueOf("P (P)").getContract(), new Contract(0, Bid.P, false, false));
        assertEquals(Auction.valueOf("P (P) P").getContract(), new Contract(0, Bid.P, false, false));
        assertEquals(Auction.valueOf("P (P) P (P)").getContract(), new Contract(0, Bid.P, false, false));
        assertEquals(Auction.valueOf("1C").getContract(), new Contract(0, Bid._1C, false, false));
        assertEquals(Auction.valueOf("P 1C").getContract(), new Contract(2, Bid._1C, false, false));
        assertEquals(Auction.valueOf("1C 1D").getContract(), new Contract(2, Bid._1D, false, false));
        assertEquals(Auction.valueOf("1C 1D P").getContract(), new Contract(2, Bid._1D, false, false));
        assertEquals(Auction.valueOf("1C (X)").getContract(), new Contract(0, Bid._1C, true, false));
        assertEquals(Auction.valueOf("1C 1D (X)").getContract(), new Contract(2, Bid._1D, true, false));
        assertEquals(Auction.valueOf("1C (X) XX").getContract(), new Contract(0, Bid._1C, true, true));
        assertEquals(Auction.valueOf("1C 1D (X) XX").getContract(), new Contract(2, Bid._1D, true, true));

        assertThrows(IllegalArgumentException.class, () -> Auction.valueOf("X"));
        assertThrows(IllegalArgumentException.class, () -> Auction.valueOf("XX"));
        assertThrows(IllegalArgumentException.class, () -> Auction.valueOf("1C X"));
        assertThrows(IllegalArgumentException.class, () -> Auction.valueOf("1C (XX)"));
        assertThrows(IllegalArgumentException.class, () -> Auction.valueOf("1C XX"));
        assertThrows(IllegalArgumentException.class, () -> Auction.valueOf("1C (X) P (XX)"));
        assertThrows(IllegalArgumentException.class, () -> Auction.valueOf("P P P"));
    }
}
