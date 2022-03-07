package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Holds the bids and inferences from the notes.
 * This last only long enough to call getBoundInferences.
 * The result of this call is what is stored.
 * 
 * @author goffster
 *
 */
public class BidInference {
    public final BidPatternList bids;
    public final InferenceList inferences;

    static final LikelyHands ALL_HANDS_LIKELY = new LikelyHands();

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

    /**
     * @return The list of inferences bound to actual bidding sequences.
     */
    public List<BoundBidInference> getBoundInferences() {
        List<BoundBidInference> result = new ArrayList<>();
        for (BiddingContext ctx : bids.getContexts()) {
            BoundBidInference inference = new BoundBidInference(ctx, inferences);
            // Catch an errors
            inference.bind(ALL_HANDS_LIKELY);
            result.add(inference);
        }
        return result;
    }
}
