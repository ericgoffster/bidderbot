package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Test;

public class BidListTest {
    @Test
    public void testValueOf() {
        assertEquals(BidList.valueOf("P"), BidList.create(List.of(Bid.P)));
        assertEquals(BidList.valueOf("P P"), BidList.create(List.of(Bid.P, Bid.P, Bid.P)));
        assertEquals(BidList.valueOf("P (P)"), BidList.create(List.of(Bid.P, Bid.P)));
        assertEquals(BidList.valueOf("1C"), BidList.create(List.of(Bid._1C)));
        assertEquals(BidList.valueOf("1C 1D"), BidList.create(List.of(Bid._1C, Bid.P, Bid._1D)));
        assertEquals(BidList.valueOf("1C (1D) 1H"), BidList.create(List.of(Bid._1C, Bid._1D, Bid._1H)));
        assertEquals(BidList.valueOf("1C (1D) 1H (X)"), BidList.create(List.of(Bid._1C, Bid._1D, Bid._1H, Bid.X)));
        assertEquals(BidList.valueOf("1C (1D) 1H (X) XX"), BidList.create(List.of(Bid._1C, Bid._1D, Bid._1H, Bid.X, Bid.XX)));
    }
    @Test
    public void testLegailty() {
        assertEquals(BidList.valueOf("P").getContract(), new Contract(0, Bid.P, false, false));
        assertEquals(BidList.valueOf("P (P)").getContract(), new Contract(0, Bid.P, false, false));
        assertEquals(BidList.valueOf("P (P) P").getContract(), new Contract(0, Bid.P, false, false));
        assertEquals(BidList.valueOf("P (P) P (P)").getContract(), new Contract(0, Bid.P, false, false));
        assertEquals(BidList.valueOf("1C").getContract(), new Contract(0, Bid._1C, false, false));
        assertEquals(BidList.valueOf("P 1C").getContract(), new Contract(2, Bid._1C, false, false));
        assertEquals(BidList.valueOf("1C 1D").getContract(), new Contract(2, Bid._1D, false, false));
        assertEquals(BidList.valueOf("1C 1D P").getContract(), new Contract(2, Bid._1D, false, false));
        assertEquals(BidList.valueOf("1C (X)").getContract(), new Contract(0, Bid._1C, true, false));
        assertEquals(BidList.valueOf("1C 1D (X)").getContract(), new Contract(2, Bid._1D, true, false));
        assertEquals(BidList.valueOf("1C (X) XX").getContract(), new Contract(0, Bid._1C, true, true));
        assertEquals(BidList.valueOf("1C 1D (X) XX").getContract(), new Contract(2, Bid._1D, true, true));
        
        assertThrows(IllegalArgumentException.class, () -> BidList.valueOf("X"));
        assertThrows(IllegalArgumentException.class, () -> BidList.valueOf("XX"));
        assertThrows(IllegalArgumentException.class, () -> BidList.valueOf("1C X"));
        assertThrows(IllegalArgumentException.class, () -> BidList.valueOf("1C (XX)"));
        assertThrows(IllegalArgumentException.class, () -> BidList.valueOf("1C XX"));
        assertThrows(IllegalArgumentException.class, () -> BidList.valueOf("1C (X) P (XX)"));
        assertThrows(IllegalArgumentException.class, () -> BidList.valueOf("P P P"));
    }
}
