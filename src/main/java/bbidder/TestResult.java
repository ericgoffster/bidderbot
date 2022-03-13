package bbidder;

public class TestResult {
    public final String where;
    public final Hand hand;
    public final BidList bids;
    public final Bid expected;
    public final Bid found;

    public TestResult(String where, Hand hand, BidList bids, Bid expected, Bid found) {
        super();
        this.where = where;
        this.hand = hand;
        this.bids = bids;
        this.expected = expected;
        this.found = found;
    }

    @Override
    public String toString() {
        return hand + " : " + bids + ": Expected " + expected + ", Got " + found;
    }

}
