package bbidder;

import java.util.Collections;
import java.util.Map;

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
}