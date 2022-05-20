package bbidder;

import bbidder.parsers.AuctionParser;
import bbidder.parsers.HandParser;
import bbidder.utils.SplitUtil;

/**
 * Represents the test of a bid.
 * 
 * @author goffster
 *
 */
public final class BiddingTest {
    public final BidInference parent;
    public final Hand hand;
    public final Auction auction;
    public final String where;
    public final boolean anti;

    /**
     * Constructs a new test
     * 
     * @param parent
     *            Parent inference
     * 
     * @param where
     *            where it was defined
     * @param hand
     *            The hand that was tested
     * @param auction
     *            The auction
     * @param anti
     *            True if this is an anti test
     */
    public BiddingTest(BidInference parent, String where, Hand hand, Auction auction, boolean anti) {
        super();
        if (hand.size() != 13) {
            throw new IllegalArgumentException("Hand does not have 13 cards: '" + hand + '"');
        }
        this.parent = parent;
        this.where = where;
        this.hand = hand;
        this.auction = auction;
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
        Auction exceptLast = auction.exceptLast();
        Bid expected = auction.getLastBid().get();
        try {
            for (Bid bid : exceptLast.getBids()) {
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
        return hand + ":" + auction;
    }

    /**
     * Parses a test.
     * 
     * @param parent
     *            Parent inference
     * 
     * @param anti
     *            True if this is an anti test
     * @param where
     *            Where the test was defined
     * @param str
     *            The string to parse
     * @return
     *         The bidding test
     */
    public static BiddingTest valueOf(BidInference parent, boolean anti, String where, String str) {
        String[] parts = SplitUtil.split(str, ":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Expected <hand>:<bids> '" + str + "'");
        }
        return new BiddingTest(parent, where, HandParser.valueOf(parts[0]), AuctionParser.valueOf(parts[1]), anti);
    }
}
