package bbidder;

import java.util.Random;

import bbidder.BiddingSystem.BidSource;

public class BiddingTest {
    public final Hand hand;
    public final BidList bids;
    public final String where;

    public BiddingTest(String where, Hand hand, BidList bids) {
        super();
        if (hand.size() != 13) {
            throw new IllegalArgumentException("Hand does not have 13 cards: '" + hand + '"');
        }
        this.where = where;
        this.hand = hand;
        this.bids = bids;
    }

    public TestResult getResult(Random r, BiddingSystem bs) {
        BiddingState state = new BiddingState(r, bs);
        BidList exceptLast = bids.exceptLast();
        for (Bid bid : exceptLast.bids) {
            state = state.withBid(bid);
        }
        Bid expected = bids.getLastBid();
        BidSource found = state.getBid(hand);
        return new TestResult(where, hand, exceptLast, expected, found);
    }

    @Override
    public String toString() {
        return hand + ":" + bids;
    }

    public static BiddingTest valueOf(String where, String str) {
        String[] parts = SplitUtil.split(str, ":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Expected <hand>:<bids> '" + str + "'");
        }
        return new BiddingTest(where, Hand.valueOf(parts[0]), BidList.valueOf2(parts[1]));
    }
}
