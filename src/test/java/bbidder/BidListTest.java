package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Test;

import bbidder.parsers.AuctionParser;

public class BidListTest {
    @Test
    public void testExceptLast() {
        assertEquals(AuctionParser.parseAuction("1C (1D) 1H (X) XX").exceptLast(), AuctionParser.parseAuction("1C (1D) 1H (X)"));
    }

    @Test
    public void testGetContract() {
        assertEquals(AuctionParser.parseAuction("1C (1D) 1H (X) XX").getContract(), new Contract(Bid._1H, true, true, 0));
        assertEquals(AuctionParser.parseAuction("1C (1D) 1H (X)").getContract(), new Contract(Bid._1H, true, false, 0));
        assertEquals(AuctionParser.parseAuction("1C (1D) 1H").getContract(), new Contract(Bid._1H, false, false, 0));
        assertEquals(AuctionParser.parseAuction("1C (1D) 1H P").getContract(), new Contract(Bid._1H, false, false, 2));
    }

    @Test
    public void testGetLastBid() {
        assertEquals(AuctionParser.parseAuction("1C (1D) 1H (X) XX").getLastBid().get(), Bid.XX);
    }

    @Test
    public void testValueOf() {
        assertEquals(AuctionParser.parseAuction("P"), Auction.create(List.of(Bid.P)));
        assertEquals(AuctionParser.parseAuction("P P"), Auction.create(List.of(Bid.P, Bid.P, Bid.P)));
        assertEquals(AuctionParser.parseAuction("P (P)"), Auction.create(List.of(Bid.P, Bid.P)));
        assertEquals(AuctionParser.parseAuction("1C"), Auction.create(List.of(Bid._1C)));
        assertEquals(AuctionParser.parseAuction("1C 1D"), Auction.create(List.of(Bid._1C, Bid.P, Bid._1D)));
        assertEquals(AuctionParser.parseAuction("1C (1D) 1H"), Auction.create(List.of(Bid._1C, Bid._1D, Bid._1H)));
        assertEquals(AuctionParser.parseAuction("1C (1D) 1H (X)"), Auction.create(List.of(Bid._1C, Bid._1D, Bid._1H, Bid.X)));
        assertEquals(AuctionParser.parseAuction("1C (1D) 1H (X) XX"), Auction.create(List.of(Bid._1C, Bid._1D, Bid._1H, Bid.X, Bid.XX)));
    }

    @Test
    public void testLegailty() {
        assertEquals(AuctionParser.parseAuction("P").getContract(), new Contract(Bid.P, false, false, 1));
        assertEquals(AuctionParser.parseAuction("P (P)").getContract(), new Contract(Bid.P, false, false, 2));
        assertEquals(AuctionParser.parseAuction("P (P) P").getContract(), new Contract(Bid.P, false, false, 3));
        assertEquals(AuctionParser.parseAuction("P (P) P (P)").getContract(), new Contract(Bid.P, false, false, 4));
        assertEquals(AuctionParser.parseAuction("1C").getContract(), new Contract(Bid._1C, false, false, 0));
        assertEquals(AuctionParser.parseAuction("P 1C").getContract(), new Contract(Bid._1C, false, false, 0));
        assertEquals(AuctionParser.parseAuction("1C 1D").getContract(), new Contract(Bid._1D, false, false, 0));
        assertEquals(AuctionParser.parseAuction("1C 1D P").getContract(), new Contract(Bid._1D, false, false, 2));
        assertEquals(AuctionParser.parseAuction("1C (X)").getContract(), new Contract(Bid._1C, true, false, 0));
        assertEquals(AuctionParser.parseAuction("1C 1D (X)").getContract(), new Contract(Bid._1D, true, false, 0));
        assertEquals(AuctionParser.parseAuction("1C (X) XX").getContract(), new Contract(Bid._1C, true, true, 0));
        assertEquals(AuctionParser.parseAuction("1C 1D (X) XX").getContract(), new Contract(Bid._1D, true, true, 0));

        assertThrows(IllegalArgumentException.class, () -> AuctionParser.parseAuction("X"));
        assertThrows(IllegalArgumentException.class, () -> AuctionParser.parseAuction("XX"));
        assertThrows(IllegalArgumentException.class, () -> AuctionParser.parseAuction("1C X"));
        assertThrows(IllegalArgumentException.class, () -> AuctionParser.parseAuction("1C (XX)"));
        assertThrows(IllegalArgumentException.class, () -> AuctionParser.parseAuction("1C XX"));
        assertThrows(IllegalArgumentException.class, () -> AuctionParser.parseAuction("1C (X) P (XX)"));
        assertThrows(IllegalArgumentException.class, () -> AuctionParser.parseAuction("P P P"));
    }
}
