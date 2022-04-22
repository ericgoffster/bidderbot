package bbidder;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    /**
     * @param generality
     *            The generality to add.
     * @return A new BiddingContext with the given generality added to the last bid.
     */
    public BidPatternContext withGeneralityAdded(Generality generality) {
        return new BidPatternContext(bid.withGeneralityAdded(generality), suits);
    }

    public Map<Symbol, BidPatternContext> resolveSymbols(Symbol symbol) {
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
        Map<Symbol, BidPatternContext> m = new LinkedHashMap<Symbol, BidPatternContext>();
        for (Symbol newSym: symbol.boundSymbols(suits)) {
            if (!suits.containsValue(newSym.getResolved())) {
                Map<String, Integer> newSuits = new HashMap<>(suits);
                newSuits.putAll(symbol.unevaluate(newSym.getResolved()));
                m.put(newSym, new BidPatternContext(bid, newSuits));
            }
        }
        return m;
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