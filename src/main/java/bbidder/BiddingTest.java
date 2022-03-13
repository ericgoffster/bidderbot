package bbidder;

public class BiddingTest {
    public final Hand hand;
    public final BidList bids;
    public BiddingTest(Hand hand, BidList bids) {
        super();
        if (hand.size() != 13) {
            throw new IllegalArgumentException("Hand does not have 13 cards: '" + hand + '"');
        }
        this.hand = hand;
        this.bids = bids;
    }
    public TestResult getResult(BiddingSystem bs) {
        BiddingState state = new BiddingState(bs);
        BidList exceptLast = bids.exceptLast();
        for (Bid bid: exceptLast.bids) {
            state = state.withBid(bid);
        }
        Bid expected = bids.getLastBid();
        Bid found = state.getBid(hand);
        return new TestResult(hand, exceptLast, expected, found);
    }
    
    @Override
    public String toString() {
        return hand + ":" + bids;
    }
    
    public static BiddingTest valueOf(String str) {
        String[] parts = SplitUtil.split(str, ":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid test '" + str + "'");
        }
        return new BiddingTest(Hand.valueOf(parts[0]), BidList.valueOf(parts[1]));
    }
}
