package bbidder;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.utils.SplitUtil;

/**
 * Holds the bids and inferences from the notes.
 * 
 * @author goffster
 *
 */
public final class BidInference {
    public final String where;
    public final BidPatternList bids;
    public final Inference inferences;

    /**
     * Constructs a bid inference
     * 
     * @param where
     *            Where it was defined
     * @param bids
     *            The list of bids
     * @param inferences
     *            The inferences you can make from the bids.
     */
    public BidInference(String where, BidPatternList bids, Inference inferences) {
        super();
        this.where = where;
        this.bids = bids;
        this.inferences = inferences;
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
     * @return A stream of bid inferences with all suit variables resolved.
     */
    public Stream<BidInference> resolveSymbols() {
        return bids.resolveSymbols(SuitTable.EMPTY)
                .flatMap(e1 -> inferences.resolveSymbols(e1.suitTable).map(e2 -> new BidInference(where, e1.getBids(), e2.getInference())));
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
