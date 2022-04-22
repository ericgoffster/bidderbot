package bbidder.symbols;

import java.util.List;
import java.util.Map;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;

public class OtherMajorSymbol implements Symbol {
    public OtherMajorSymbol() {
        super();
    }

    @Override
    public String toString() {
        return "OM";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

    @Override
    public Symbol evaluate(Map<String, Integer> suits) {
        if (suits.containsKey("M")) {
            return new ConstSymbol(otherMajor(suits.get("M")));
        }
        if (suits.containsKey("OM")) {
            return new ConstSymbol(suits.get("OM"));          
        }
        return null;
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public Map<String, Integer> unevaluate(int strain) {
        return Map.of("OM", strain);
    }

    @Override
    public List<Symbol> boundSymbols(Map<String, Integer> suits) {
        if (suits.containsKey("M") || suits.containsKey("OM")) {
            return List.of(evaluate(suits));
        }
        return List.of(new ConstSymbol(Constants.HEART), new ConstSymbol(Constants.SPADE));
    }

    private static Integer otherMajor(Integer strain) {
        if (strain == null) {
            return null;
        }
        switch (strain.intValue()) {
        case Constants.HEART:
            return Constants.SPADE;
        case Constants.SPADE:
            return Constants.HEART;
        default:
            throw new IllegalArgumentException("invalid major");
        }
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        return true;
    }
}
