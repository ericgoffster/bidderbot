package bbidder;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import bbidder.inferences.AndInference;

public final class BiddingContext {
    static Pattern SUIT_PATTERN = Pattern.compile("(.*)\\-(\\d+)");
    public final Inference inference;

    private final Map<String, Integer> suits;

    public BiddingContext(Inference inference, Map<String, Integer> suits) {
        super();
        this.inference = inference;
        this.suits = suits;
    }
    
    /**
     * @return The immutable symbol table.
     */
    public Map<String, Integer> getSuits() {
        return Collections.unmodifiableMap(suits);
    }

    public BiddingContext withInferenceAdded(Inference inf) {
        return new BiddingContext(AndInference.create(inference, inf), suits);
    }

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
                m.put(newSym, new BiddingContext(inference, newSuits));
            }
        }
        return m;
    }

    @Override
    public String toString() {
        if (suits.isEmpty()) {
            return inference.toString();
        }
        return inference + " where " + suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inference, suits);
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
        return Objects.equals(inference, other.inference) && Objects.equals(suits, other.suits);
    }
}