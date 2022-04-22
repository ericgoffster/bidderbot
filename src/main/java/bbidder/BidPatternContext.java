package bbidder;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class BidPatternContext {
    public final BidPattern bid;

    private final Map<String, Integer> suits;

    public BidPatternContext(BidPattern bid, Map<String, Integer> suits) {
        super();
        this.bid = bid;
        this.suits = suits;
    }
    
    /**
     * @return The immutable symbol table.
     */
    public Map<String, Integer> getSuits() {
        return Collections.unmodifiableMap(suits);
    }

    @Override
    public String toString() {
        if (suits.isEmpty()) {
            return bid.toString();
        }
        return bid + " where " + suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bid, suits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidPatternContext other = (BidPatternContext) obj;
        return Objects.equals(bid, other.bid) && Objects.equals(suits, other.suits);
    }
}