package bbidder;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A bidding context is used to build BidInference's.
 * A bidding context consists of the current BidInference, and the current symbol table.
 * A call to resolveSymbols will create "N" different version of the bidding context,
 * each with a different value of the suit that is allowed, added toe the symbol table.
 * 
 * Note that a BiddingContext is immutable.
 * 
 * @author goffster
 *
 */
public final class BiddingContext {
    public static final BiddingContext EMPTY = new BiddingContext(BidInference.EMPTY, Map.of());
    static Pattern SUIT_PATTERN = Pattern.compile("(.*)\\-(\\d+)");
    private final BidInference bidInference;

    private final Map<String, Integer> suits;

    private BiddingContext(BidInference bidInference, Map<String, Integer> suits) {
        super();
        this.bidInference = bidInference;
        this.suits = suits;
    }
    
    public BiddingContext at(String where) {
        return new BiddingContext(bidInference.at(where), suits);
    }

    /**
     * @return The bid inference.
     */
    public BidInference getInference() {
        return bidInference;
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
    public BiddingContext withBidAdded(BidPattern patt) {
        return new BiddingContext(bidInference.withBidAdded(patt), suits);
    }

    /**
     * @param patt
     *            The bid pattern to add.
     * @return A new BiddingContext with the last bid replaced.
     */
    public BiddingContext withLastBidReplaced(BidPattern patt) {
        return new BiddingContext(bidInference.withLastBidReplaced(patt), suits);
    }

    /**
     * @param inf
     *            The inference to add.
     * @return A new BiddingContext with the given inference added to the inference list.
     */
    public BiddingContext withInferenceAdded(Inference inf) {
        return new BiddingContext(bidInference.withInferenceAdded(inf), suits);
    }

    /**
     * @param generality
     *            The generality to add.
     * @return A new BiddingContext with the given generality added to the last bid.
     */
    public BiddingContext withGeneralityAdded(Generality generality) {
        return withLastBidReplaced(bidInference.bids.getLastBid().withGeneralityAdded(generality));
    }

    /**
     * @param symbol
     *            The symbol
     * @return (0,1,2,3,4) for a given symbol, null if not found.
     */
    public Integer getSuit(Symbol symbol) {
        Symbol evaluate = symbol.evaluate(suits);
        if (evaluate == null) {
            return null;
        }
        return evaluate.getResolved();
    }

    /**
     * @param symbol
     *            The suit symbol to match.
     * @return a map of strains to new bidding contexts. Each key in the map represents a possible bid strain
     *         for the given suit symbol.
     */
    public Map<Symbol, BiddingContext> resolveSymbols(Symbol symbol) {
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
        Map<Symbol, BiddingContext> m = new LinkedHashMap<Symbol, BiddingContext>();
        for (Symbol newSym: symbol.boundSymbols(suits)) {
            if (!suits.containsValue(newSym.getResolved())) {
                Map<String, Integer> newSuits = new HashMap<>(suits);
                newSuits.putAll(symbol.unevaluate(newSym.getResolved()));
                m.put(newSym, new BiddingContext(bidInference, newSuits));
            }
        }
        return m;
    }

    @Override
    public String toString() {
        if (suits.isEmpty()) {
            return bidInference.toString();
        }
        return bidInference + " where " + suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bidInference, suits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BiddingContext other = (BiddingContext) obj;
        return Objects.equals(bidInference, other.bidInference) && Objects.equals(suits, other.suits);
    }
}