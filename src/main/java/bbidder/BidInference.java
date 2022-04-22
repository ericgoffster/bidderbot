package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.inferences.AndInference;
import bbidder.inferences.TrueInference;

/**
 * Holds the bids and inferences from the notes.
 * 
 * @author goffster
 *
 */
public class BidInference {
    public static BidInference EMPTY = new BidInference("System", BidPatternList.EMPTY, TrueInference.T);
    public final String where;
    public final BidPatternList bids;
    public final Inference inferences;

    private BidInference(String where, BidPatternList bids, Inference inferences) {
        super();
        this.where = where;
        this.bids = bids;
        this.inferences = inferences;
    }
    
    public BidInference at(String where) {
        return new BidInference(where, bids, inferences);
    }
    
    public BidInference withBids(BidPatternList bids) {
        return new BidInference(where, bids, inferences);
    }
    public BidInference withInference(Inference inferences) {
        return new BidInference(where, bids, inferences);
    }

    /**
     * @param patt
     *            The bid pattern to add
     * @return A bid inference with the given bid pattern added
     */
    public BidInference withBidAdded(BidPattern patt) {
        return new BidInference(where, bids.withBidAdded(patt), inferences);
    }

    /**
     * 
     * @param patt
     *            The bid pattern to add
     * @return A bid inference with the last bid pattern replaced.
     */
    public BidInference withLastBidReplaced(BidPattern patt) {
        return new BidInference(where, bids.withLastBidReplaced(patt), inferences);
    }

    /**
     * 
     * @param inf
     *            The inference to add
     * @return A bid inference with the given inference added
     */
    public BidInference withInferenceAdded(Inference inf) {
        return new BidInference(where, bids, AndInference.create(inferences, inf));
    }

    /**
     * @param where
     *            Where is it located.
     * @param reg
     *            The inference registry
     * @param str
     *            The string to parse.
     * @return A BidInference parsed from the string
     */
    public static BidInference valueOf(String where, InferenceRegistry reg, String str) {
        if (str == null) {
            return null;
        }
        String[] parts = SplitUtil.split(str, "=>", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid bid inference '" + str + "'");
        }
        return new BidInference(where, BidPatternList.valueOf(reg, parts[0]), reg.parseInference(parts[1]));
    }

    /**
     * @return A list of bid inferences with all suit variables resolved.
     */
    public List<BidInference> resolveSymbols() {
        List<BidInference> result = new ArrayList<>();
        for (BidPatternListContext bc3 : bids.resolveFirstSymbol()) {
            BiddingContext bc2 = new BiddingContext(TrueInference.T, bc3.getSuits());
            for (BiddingContext bc : inferences.resolveSymbols(bc2)) {
                result.add(BidInference.EMPTY.at(where).withBids(bc3.bids).withInference(bc.inference));
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
