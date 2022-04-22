package bbidder;

public final class TestResult {
    public final String where;
    public final Hand hand;
    public final Auction bids;
    public final Bid expected;
    public final BidSource found;
    public final BiddingState state;
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
