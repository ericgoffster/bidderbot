package bbidder;

import java.util.Objects;

import bbidder.utils.MyStream;

/**
 * Holds the bids and inferences from the notes.
 * 
 * @author goffster
 *
 */
public final class BidInference {
    public final String description;
    public final String where;
    public final BidPatternList bids;
    public final Inference inferences;

    /**
     * Constructs a bid inference
     * 
     * @param description
     *            Bid description
     * @param where
     *            Where it was defined
     * @param bids
     *            The list of bids
     * @param inferences
     *            The inferences you can make from the bids.
     */
    public BidInference(String description, String where, BidPatternList bids, Inference inferences) {
        super();
        this.description = description;
        this.where = where;
        this.bids = bids;
        this.inferences = inferences;
    }

    /**
     * @return A stream of bid inferences with all suit variables resolved.
     */
    public MyStream<BidInference> resolveSuits() {
        return bids.resolveSuits(SuitTable.EMPTY)
                .flatMap(e1 -> inferences.resolveSuits(e1.suitTable)
                        .map(e2 -> new BidInference(e2.suitTable.fixDescription(description), where, e1.getBids(), e2.getInference())));
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
