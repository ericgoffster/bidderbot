package bbidder.symbols;

import static bbidder.Constants.ALL_SUITS;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import bbidder.Bid;
import bbidder.Symbol;

public class VarSymbol implements Symbol {
    public final String v;

    public VarSymbol(String v) {
        super();
        this.v = v;
    }

    @Override
    public String toString() {
        return v;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VarSymbol other = (VarSymbol) obj;
        return Objects.equals(v, other.v);
    }

    @Override
    public Integer evaluate(Map<String, Integer> suits) {
        return suits.get(v);
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }

    @Override
    public Map<String, Integer> unevaluate(int strain) {
        return Map.of(v, strain);
    }

    @Override
    public short getSuitClass(Map<String, Integer> suits) {
        if (suits.containsKey(v)) {
            return (short) (1 << evaluate(suits));
        }
        return ALL_SUITS;
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
