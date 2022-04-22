package bbidder;

/**
 * The result from running a test.
 * 
 * @author goffster
 *
 */
public final class TestResult {
    /**
     * Where the test was located.
     */
    public final String where;

    /**
     * The hand that was tested.
     */
    public final Hand hand;

    /**
     * The action that was tested.
     */
    public final Auction bids;

    /**
     * The expected bid.
     */
    public final Bid expected;

    /**
     * The bid that was found (and associated information)
     */
    public final BidSource found;

    /**
     * The state of the hand when the bid was made.
     */
    public final BiddingState state;

    /**
     * Any exception that might have been thrown.
     */
    public final Exception ex;

    public TestResult(String where, Hand hand, Auction bids, Bid expected, BidSource found, BiddingState state, Exception ex) {
        super();
        this.where = where;
        this.hand = hand;
        this.bids = bids;
        this.expected = expected;
        this.found = found;
        this.state = state;
        this.ex = ex;
    }

    @Override
    public String toString() {
        return hand + " : " + bids + ": Expected " + expected + ", Got " + found;
    }

}
