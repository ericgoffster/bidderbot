package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class BiddingSystemTest {
    @Test
    public void test1() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test1.bidding"));
        {
            List<BoundInference> l = bs.getInference(new BidList(List.of(Bid._1H)));
            assertEquals(1, l.size());
            assertTrue(l.get(0).matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
        {
            List<BoundInference> l = bs.getInference(new BidList(List.of(Bid._1S)));
            assertEquals(1, l.size());
            assertFalse(l.get(0).matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
    }
    @Test
    public void test1n() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test1.bidding"));
        List<BoundInference> l = bs.getInference(new BidList(List.of(Bid._1C)));
        assertEquals(0, l.size());
    }
    @Test
    public void test2() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test2.bidding"));
        List<BoundInference> l = bs.getInference(new BidList(List.of(Bid._1H, Bid._2C)));
        assertEquals(1, l.size());
    }
    @Test
    public void test2n() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test2.bidding"));
        List<BoundInference> l = bs.getInference(new BidList(List.of(Bid._1H, Bid._2H)));
        assertEquals(0, l.size());
    }
    @Test
    public void test3() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test3.bidding"));
        {
            List<BoundInference> l = bs.getInference(new BidList(List.of(Bid._1H)));
            assertEquals(1, l.size());
            assertFalse(l.get(0).matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
        {
            List<BoundInference> l = bs.getInference(new BidList(List.of(Bid._1S)));
            assertEquals(1, l.size());
            assertTrue(l.get(0).matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
    }
}
