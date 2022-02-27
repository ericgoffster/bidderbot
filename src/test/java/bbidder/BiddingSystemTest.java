package bbidder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import bbidder.inferences.ConstBoundInference;

public class BiddingSystemTest {
    @Test
    public void test1() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test1.bidding"));
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1H)));
            assertTrue(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1S)));
            assertFalse(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
    }

    @Test
    public void test1n() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test1.bidding"));
        IBoundInference l = bs.getInference(new BidList(List.of(Bid._1C)));
        assertTrue(l instanceof ConstBoundInference && !((ConstBoundInference) l).result);
    }

    @Test
    public void test2() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test2.bidding"));
        IBoundInference l = bs.getInference(new BidList(List.of(Bid._1H, Bid._2C)));
        assertTrue(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
    }

    @Test
    public void test2n() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test2.bidding"));
        IBoundInference l = bs.getInference(new BidList(List.of(Bid._1H, Bid._2H)));
        assertTrue(l instanceof ConstBoundInference && !((ConstBoundInference) l).result);
    }

    @Test
    public void test3() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test3.bidding"));
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1H)));
            assertFalse(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1S)));
            assertTrue(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
    }
    
    @Test
    public void test4() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/2over1.bidding"));
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1N)));
            assertFalse(l.matches(Hand.valueOf("AKQ KQT 234 2345")));
            assertTrue(l.matches(Hand.valueOf("AKQ KQJ 234 2345")));
            assertTrue(l.matches(Hand.valueOf("AKQ AKJ 234 2345")));
            assertFalse(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
    }
}
