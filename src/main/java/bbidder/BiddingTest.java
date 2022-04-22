package bbidder;

/**
 * Represents the test of a bid.
 * 
 * @author goffster
 *
 */
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

    /**
     * Retrieve a result for the current test
     * 
     * @param bs
     *            The bidding system
     * @return
     *         The result after running the test
     */
    public TestResult getResult(BiddingSystem bs) {
        BiddingState state = new BiddingState(bs);
        BidList exceptLast = bids.exceptLast();
        for (Bid bid : exceptLast.getBids()) {
            DebugUtils.breakpoint();
            state = state.withBid(bid);
        }
        Bid expected = bids.getLastBid();
        BidSource found = state.getBid(hand);
        return new TestResult(where, hand, exceptLast, expected, found, state);
    }

    @Override
    public String toString() {
        return hand + ":" + bids;
    }

    /**
     * Parses a test.
     * 
     * @param where
     *            Where the test was defined
     * @param str
     *            The string to parse
     * @return
     *         The bidding test
     */
    public static BiddingTest valueOf(String where, String str) {
        String[] parts = SplitUtil.split(str, ":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Expected <hand>:<bids> '" + str + "'");
        }
        return new BiddingTest(where, Hand.valueOf(parts[0]), BidList.valueOf(parts[1]));
    }
}
