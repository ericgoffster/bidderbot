package bbidder;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class BidPatternListContext {
    public final BidPatternList bids;

    private final Map<String, Integer> suits;

    public BidPatternListContext(BidPatternList bids, Map<String, Integer> suits) {
        super();
        this.bids = bids;
        this.suits = suits;
    }
    
    /**
     * @return The immutable symbol table.
     */
    public Map<String, Integer> getSuits() {
        return Collections.unmodifiableMap(suits);
    }

    /**
     * @param patt
     *            The bid pattern to add.
     * @return A new BiddingContext with the given bid added to the bid list.
     */
    public BidPatternListContext withBidAdded(BidPattern patt) {
        return new BidPatternListContext(bids.withBidAdded(patt), suits);
    }

    @Override
    public String toString() {
        if (suits.isEmpty()) {
            return bids.toString();
        }
        return bids + " where " + suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids, suits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidPatternListContext other = (BidPatternListContext) obj;
        return Objects.equals(bids, other.bids) && Objects.equals(suits, other.suits);
    }
}