package bbidder.symbols;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import bbidder.Bid;
import bbidder.Constants;
import bbidder.Symbol;

public class MinorSymbol implements Symbol {
    public MinorSymbol() {
        super();
    }

    @Override
    public String toString() {
        return "m";
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
    public Integer evaluate(Map<String, Integer> suits) {
        if (suits.containsKey("om")) {
            return otherMinor(suits.get("om"));
        }
        return suits.get("m");
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public Map<String, Integer> unevaluate(int strain) {
        return Map.of("m", strain);
    }
    
    @Override
    public List<Symbol> boundSymbols(Map<String, Integer> suits) {
        if (suits.containsKey("m") || suits.containsKey("om")) {
            return List.of(new BoundSymbol(evaluate(suits), this));
        }
        return List.of(new BoundSymbol(Constants.CLUB, this), new BoundSymbol(Constants.DIAMOND, this));
    }

    private static Integer otherMinor(Integer strain) {
        if (strain == null) {
            return null;
        }
        switch (strain.intValue()) {
        case Constants.CLUB:
            return Constants.DIAMOND;
        case Constants.DIAMOND:
            return Constants.CLUB;
        default:
            throw new IllegalArgumentException("invalid minor");
        }
    }
    
    @Override
    public Comparator<Symbol> direction() {
        return (a, b) -> Integer.compare(a.getResolved(), b.getResolved());
    }    


    @Override
    public Predicate<Bid> levelTest() {
        return level -> true;
    }
}
