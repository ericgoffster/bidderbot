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
    public final String where;
    public final BidPatternList bids;
    public final InferenceList inferences;

    public BidInference(String where, BidPatternList bids, InferenceList inferences) {
        super();
        this.where = where;
        this.bids = bids;
        this.inferences = inferences;
    }
    
    public BidInference at(String where) {
        return new BidInference(where, bids, inferences);
    }
    
    public BidInference withBidAdded(BidPattern patt) {
        return new BidInference(where, bids.withBidAdded(patt), inferences);
    }

    public BidInference withInferenceAdded(Inference i) {
        return new BidInference(where, bids, inferences.withInferenceAdded(i));
    }

    /**
     * @param reg
     *            The inference registry
     * @param str
     *            The string to parse.
     * @return A BidInference parsed from the string
     */
    public static BidInference valueOf(InferenceRegistry reg, String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "=>", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid bid inference '" + str + "'");
        }
        return new BidInference(null, BidPatternList.valueOf(parts[0]), InferenceList.valueOf(reg, parts[1]));
    }

    public List<BidInference> resolveSuits() {
        List<BidInference> result = new ArrayList<>();
        for (BiddingContext ctx : bids.resolveSuits()) {
            for(BiddingContext mil: inferences.resolveSuits(ctx)) {
                // Catch any errors
                result.add(mil.getInference());
            }
        }
        return result;
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
}
