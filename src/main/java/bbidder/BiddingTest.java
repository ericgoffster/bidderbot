package bbidder;

/**
 * Represents the test of a bid.
 * 
 * @author goffster
 *
 */
public final class BiddingTest {
    public final Hand hand;
    public final Auction bids;
    public final String where;
    public final boolean anti;

    public BiddingTest(String where, Hand hand, Auction bids, boolean anti) {
        super();
        if (hand.size() != 13) {
            throw new IllegalArgumentException("Hand does not have 13 cards: '" + hand + '"');
        }
        this.where = where;
        this.hand = hand;
        this.bids = bids;
        this.anti = anti;
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
        Auction exceptLast = bids.exceptLast();
        Bid expected = bids.getLastBid();
        try {
            for (Bid bid : exceptLast.getBids()) {
                DebugUtils.breakpoint();
                state = state.withBid(bid);
            }
            BidSource found = state.getBid(hand);
            return new TestResult(where, hand, exceptLast, expected, found, state, null);
        } catch (Exception ex) {
            return new TestResult(where, hand, exceptLast, expected, null, state, ex);
        }
    }

    @Override
    public String toString() {
        return hand + ":" + bids;
    }

    /**
     * Parses a test.
     * @param anti 
     *            True if this is an anti test
     * @param where
     *            Where the test was defined
     * @param str
     *            The string to parse
     * @return
     *         The bidding test
     */
    public static BiddingTest valueOf(boolean anti, String where, String str) {
        String[] parts = SplitUtil.split(str, ":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Expected <hand>:<bids> '" + str + "'");
        }
        return new BiddingTest(where, Hand.valueOf(parts[0]), Auction.valueOf(parts[1]), anti);
    }
}
