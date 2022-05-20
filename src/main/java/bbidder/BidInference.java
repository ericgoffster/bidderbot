package bbidder;

import java.util.Objects;

import bbidder.parsers.InferenceParser;
import bbidder.utils.MyStream;
import bbidder.utils.SplitUtil;

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
     * @param where
     *            Where is it located.
     * @param str
     *            The string to parse.
     * @param prefix
     *            The prefix
     * @return A BidInference parsed from the string
     */
    public static BidInference valueOf(String where, String str, BidPatternList prefix) {
        if (str == null) {
            return null;
        }
        String description;
        int pos = str.indexOf("[[");
        if (pos >= 0) {
            int pos2 = str.indexOf("]]", pos);
            if (pos >= 0 && pos2 >= 0) {
                description = str.substring(pos + 2, pos2);
                str = str.substring(0, pos) + str.substring(pos2 + 2);
            } else {
                description = "";
            }
        } else {
            description = "";
        }
        String[] parts = SplitUtil.split(str, "=>", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid bid inference '" + str + "'");
        }
        for (BidPattern pattern : BidPatternList.valueOf(parts[0]).getBids()) {
            prefix = prefix.withBidAdded(pattern);
        }
        return new BidInference(description, where, prefix, InferenceParser.parseInference(parts[1]));
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
