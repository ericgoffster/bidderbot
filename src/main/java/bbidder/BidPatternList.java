package bbidder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Represents the bid patterns as read in from the notes.
 * This structure lasts only long enough to call getContexts() which is what is actually used
 * in the compiled version of the notes.
 * 
 * @author goffster
 *
 */
public class BidPatternList {
    public final List<BidPattern> bids;

    public BidPatternList(List<BidPattern> bids) {
        super();
        this.bids = bids;
    }

    /*
     * Retrieves the list of bidding contexts for this bid pattern list.
     */
    public List<BiddingContext> getContexts() {
        HashMap<String, Integer> suits = new HashMap<>();
        BiddingContext ctx = new BiddingContext(new BidList(List.of()), suits);

        // no patterns, then a wide open context.
        if (bids.isEmpty()) {
            return List.of(ctx);
        }

        List<BiddingContext> l = new ArrayList<>();
        // Add in first hand passing
        BidPattern pattern = bids.get(0);
        getContexts(l, new BiddingContext(new BidList(List.of(Bid.P, Bid.P)), suits), true);
        getContexts(l, new BiddingContext(new BidList(List.of(Bid.P)), suits), true);
        getContexts(l, ctx, true);
        if (!pattern.isOpposition) {
            getContexts(l, ctx, false);
        }
        return l;
    }

    public static BidPatternList valueOf(String str) {
        if (str == null) {
            return null;
        }
        List<BidPattern> l = new ArrayList<>();
        for (String part : SplitUtil.split(str, "\\s+")) {
            l.add(BidPattern.valueOf(part));
        }
        return new BidPatternList(l);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (BidPattern bid : bids) {
            sb.append(delim).append(bid);
            delim = " ";
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidPatternList other = (BidPatternList) obj;
        return Objects.equals(bids, other.bids);
    }

    private void getContexts(List<BiddingContext> l, BiddingContext ctx, boolean isOpp) {
        if (bids.isEmpty()) {
            if (!isOpp) {
                throw new IllegalArgumentException("last bid must be made by 'we'");
            }
            l.add(ctx);
            return;
        }
        // If it is the opps turn and the next bid is not opp, then assume pass for opps
        BidPattern pattern = bids.get(0);
        if (isOpp && !pattern.isOpposition) {
            getContexts(l, new BiddingContext(ctx.bids.withBidAdded(Bid.P), ctx.suits), !isOpp);
            return;
        }

        BidPatternList theRest = new BidPatternList(bids.subList(1, bids.size()));

        for (Bid bid : ctx.getBids(pattern)) {
            theRest.getContexts(l, ctx.withNewBid(bid, pattern), !isOpp);
        }
    }
}
