package bbidder;

import java.util.Collections;
import java.util.Map;

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
}