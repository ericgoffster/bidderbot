package bbidder.symbols;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Constants;
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
    public List<Symbol> boundSymbols(Map<String, Integer> suits) {
        if (suits.containsKey(v)) {
            return List.of(new BoundSymbol(evaluate(suits), this));
        }
        return List.of(new BoundSymbol(Constants.CLUB, this), new BoundSymbol(Constants.DIAMOND, this), new BoundSymbol(Constants.HEART, this), new BoundSymbol(Constants.SPADE, this));
    }

    @Override
    public boolean compatibleWith(Bid bid) {
        return true;
    }
}
