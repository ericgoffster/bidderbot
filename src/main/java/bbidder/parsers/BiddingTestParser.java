package bbidder.parsers;

import bbidder.BidInference;
import bbidder.BiddingTest;
import bbidder.utils.SplitUtil;

public class BiddingTestParser {

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
    public static BiddingTest parseBiddingTest(BidInference parent, boolean anti, String where, String str) {
        String[] parts = SplitUtil.split(str, ":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Expected <hand>:<bids> '" + str + "'");
        }
        return new BiddingTest(parent, where, HandParser.parseHand(parts[0]), AuctionParser.parseAuction(parts[1]), anti);
    }

}
