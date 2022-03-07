package bbidder;

import java.util.Map;
import java.util.Objects;

public class BidInference {
    public final BidPatternList bids;
    public final InferenceList inferences;

    public BidInference(BidPatternList bids, InferenceList inferences) {
        super();
        this.bids = bids;
        this.inferences = inferences;
    }

    public static BidInference valueOf(InferenceRegistry reg, String str) {
        int pos = str.indexOf("=>");
        return new BidInference(BidPatternList.valueOf(str.substring(0, pos)), InferenceList.valueOf(reg, str.substring(pos + 2)));
    }

    @Override
    public String toString() {
        return bids + " => " + inferences;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids, inferences);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidInference other = (BidInference) obj;
        return Objects.equals(bids, other.bids) && Objects.equals(inferences, other.inferences);
    }
    
    class BidCtx {
        final BidList boundBidList;
        final Bid lastBidSuit;
        final Map<String, Integer> suits;
        public BidCtx(BidList boundBidList, Bid lastBidSuit, Map<String, Integer> suits) {
            super();
            this.boundBidList = boundBidList;
            this.lastBidSuit = lastBidSuit;
            this.suits = suits;
        }
    }
}
