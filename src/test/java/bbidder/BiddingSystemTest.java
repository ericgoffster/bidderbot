package bbidder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import bbidder.inferences.ConstBoundInference;

public class BiddingSystemTest {
    LikelyHands likelyHands = new LikelyHands();

    @Test
    public void test1() throws IOException {
        BiddingSystem bs = BiddingSystem.load("", "classpath:test1.bidding", ex -> {
            ex.printStackTrace();
        });
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1H)), likelyHands);
            assertTrue(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1S)), likelyHands);
            assertFalse(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
    }

    @Test
    public void test1n() throws IOException {
        BiddingSystem bs = BiddingSystem.load("", "classpath:test1.bidding", ex -> {
            ex.printStackTrace();
        });
        IBoundInference l = bs.getInference(new BidList(List.of(Bid._1C)), likelyHands);
        assertTrue(l instanceof ConstBoundInference && !((ConstBoundInference) l).result);
    }

    @Test
    public void test2() throws IOException {
        BiddingSystem bs = BiddingSystem.load("", "classpath:test2.bidding", ex -> {
            ex.printStackTrace();
        });
        IBoundInference l = bs.getInference(new BidList(List.of(Bid._1H, Bid.P, Bid._2C)), likelyHands);
        assertTrue(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
    }

    @Test
    public void test2n() throws IOException {
        BiddingSystem bs = BiddingSystem.load("", "classpath:test2.bidding", ex -> {
            ex.printStackTrace();
        });
        IBoundInference l = bs.getInference(new BidList(List.of(Bid._1H, Bid.P, Bid._2H)), likelyHands);
        assertTrue(l instanceof ConstBoundInference && !((ConstBoundInference) l).result);
    }

    @Test
    public void test3() throws IOException {
        BiddingSystem bs = BiddingSystem.load("", "classpath:test3.bidding", ex -> {
            ex.printStackTrace();
        });
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1H)), likelyHands);
            assertFalse(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1S)), likelyHands);
            assertTrue(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));
        }
    }

    @Test
    public void test4() throws IOException {
        BiddingSystem bs = BiddingSystem.load("", "classpath:2over1.bidding", ex -> {
            ex.printStackTrace();
        });
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1N)), likelyHands);
            assertFalse(l.matches(Hand.valueOf("AKQ KQT 234 2345")));
            assertTrue(l.matches(Hand.valueOf("AKQ KQJ 234 2345")));
            assertTrue(l.matches(Hand.valueOf("AKQ AKJ 234 2345")));
            assertFalse(l.matches(Hand.valueOf("AKQ AKQ 234 2345")));

            HandList hands = HandGenerator.generateHands(l, 100);
            assertTrue(hands.minHcp() >= 15);
            assertTrue(hands.maxHcp() <= 17);
            assertTrue(hands.minInSuit(0) >= 2);
            assertTrue(hands.minInSuit(1) >= 2);
            assertTrue(hands.minInSuit(2) >= 2);
            assertTrue(hands.minInSuit(3) >= 2);
        }
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1S)), likelyHands);
            assertFalse(l.matches(Hand.valueOf("AKQ KQT 234 2345")));
            assertTrue(l.matches(Hand.valueOf("AQJ32 AQ2 23 456")));
            assertTrue(l.matches(Hand.valueOf("AQJ32 AQ234 2 45")));
            assertFalse(l.matches(Hand.valueOf("AQJ32 AQ2345 2 4")));
            assertFalse(l.matches(Hand.valueOf("AQJ32 234 23 456")));

            HandList hands = HandGenerator.generateHands(l, 100);
            assertTrue(hands.minHcp() >= 13);
            assertTrue(hands.minInSuit(0) >= 0);
            assertTrue(hands.minInSuit(1) >= 0);
            assertTrue(hands.minInSuit(2) >= 0);
            assertTrue(hands.minInSuit(3) >= 5);
        }
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1H)), likelyHands);
            assertFalse(l.matches(Hand.valueOf("AKQ KQT 234 2345")));
            assertTrue(l.matches(Hand.valueOf("AQ2 AQJ32 23 456")));
            assertFalse(l.matches(Hand.valueOf("AQJ32 AQ234 2 45")));
            assertTrue(l.matches(Hand.valueOf("AQJ32 AQ2345 2 4")));
            assertFalse(l.matches(Hand.valueOf("234 AQJ32 23 456")));

            HandList hands = HandGenerator.generateHands(l, 100);
            assertTrue(hands.minHcp() >= 13);
            assertTrue(hands.minInSuit(0) >= 0);
            assertTrue(hands.minInSuit(1) >= 0);
            assertTrue(hands.minInSuit(2) >= 5);
            assertTrue(hands.minInSuit(3) >= 0);
        }
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1C)), likelyHands);
            assertFalse(l.matches(Hand.valueOf("AKQ23 2 23 AQ234")));
            assertTrue(l.matches(Hand.valueOf("AKQ23 2 3 AQ2345")));
            assertTrue(l.matches(Hand.valueOf("AKQ KQT 234 2345")));
            assertTrue(l.matches(Hand.valueOf("AKQ KQT2 234 234")));
            assertFalse(l.matches(Hand.valueOf("AKQ KQ 2345 2345")));

            HandList hands = HandGenerator.generateHands(l, 100);
            assertTrue(hands.minHcp() >= 13);
            assertTrue(hands.minInSuit(0) >= 3);
            assertTrue(hands.minInSuit(1) >= 0);
            assertTrue(hands.minInSuit(2) >= 0);
            assertTrue(hands.minInSuit(3) >= 0);
        }
        {
            IBoundInference l = bs.getInference(new BidList(List.of(Bid._1D)), likelyHands);
            assertFalse(l.matches(Hand.valueOf("AKQ23 2 AQ234 23")));
            assertTrue(l.matches(Hand.valueOf("AKQ23 2 AQ2345 3")));
            assertTrue(l.matches(Hand.valueOf("AKQ KQT 2345 234")));
            assertFalse(l.matches(Hand.valueOf("AKQ KQT2 234 234")));
            assertTrue(l.matches(Hand.valueOf("AKQ KQ 2345 2345")));

            HandList hands = HandGenerator.generateHands(l, 100);
            assertTrue(hands.minHcp() >= 13);
            assertTrue(hands.minInSuit(0) >= 0);
            assertTrue(hands.minInSuit(1) >= 3);
            assertTrue(hands.minInSuit(2) >= 0);
            assertTrue(hands.minInSuit(3) >= 0);
        }
    }
}
