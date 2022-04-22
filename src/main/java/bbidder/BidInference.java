package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * @return A list of bid inferences with all suit variables resolved.
     */
    public List<BidInference> resolveSymbols() {
        List<BidInference> result = new ArrayList<>();
        for (BidPatternListContext ctx1 : bids.resolveSymbols(SymbolTable.EMPTY)) {
            for (InferenceContext ctx2 : inferences.resolveSymbols(ctx1.symbols)) {
                result.add(new BidInference(where, ctx1.bids, ctx2.inference));
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
