package bbidder;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public final class BidPatternListContext {
    static Pattern SUIT_PATTERN = Pattern.compile("(.*)\\-(\\d+)");
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

    /**
     * @param symbol
     *            The suit symbol to match.
     * @return a map of strains to new bidding contexts. Each key in the map represents a possible bid strain
     *         for the given suit symbol.
     */
    public Map<Symbol, BidPatternListContext> resolveSymbols(Symbol symbol) {
        {
            Symbol esymbol = symbol.evaluate(suits);
            if (esymbol != null) {
                int strain = esymbol.getResolved();
                if (strain < 0 || strain > 4) {
                    throw new IllegalArgumentException("Invalid strain");
                }
                return Map.of(esymbol, this);
            }
        }
        Map<Symbol, BidPatternListContext> m = new LinkedHashMap<Symbol, BidPatternListContext>();
        for (Symbol newSym: symbol.boundSymbols(suits)) {
            if (!suits.containsValue(newSym.getResolved())) {
                Map<String, Integer> newSuits = new HashMap<>(suits);
                newSuits.putAll(symbol.unevaluate(newSym.getResolved()));
                m.put(newSym, new BidPatternListContext(bids, newSuits));
            }
        }
        return m;
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