package bbidder.parsers;

import java.util.ArrayList;
import java.util.List;

import bbidder.Auction;
import bbidder.Bid;
import bbidder.utils.SplitUtil;

public class AuctionParser {

    /**
     * @param str
     *            The string to parse
     * @return A bid list parsed from the string
     * @throws IllegalArgumentException
     *             If the auction is not valid or bids cant be parsed.
     */
    public static Auction valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "\\s+");
        List<Bid> bids = new ArrayList<>();
        boolean we = false;
        boolean first = true;
        for (String part : parts) {
            if (part.startsWith("(") && part.endsWith(")")) {
                if (!first && !we) {
                    bids.add(Bid.P);
                }
                Bid b = Bid.fromStr(part.substring(1, part.length() - 1));
                if (b == null) {
                    throw new IllegalArgumentException("Illegal bid: '" + part + "'");
                }
                bids.add(b);
                we = false;
            } else {
                if (!first && we) {
                    bids.add(Bid.P);
                }
                Bid b = Bid.fromStr(part);
                if (b == null) {
                    throw new IllegalArgumentException("Illegal bid: '" + part + "'");
                }
                bids.add(b);
                we = true;
            }
            first = false;
        }
        return Auction.create(bids);
    }

}
