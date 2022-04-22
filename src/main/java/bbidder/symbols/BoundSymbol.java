package bbidder.symbols;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Strain;
import bbidder.Symbol;

public class BoundSymbol implements Symbol {
    public final int strain;
    private final Symbol from;

    public BoundSymbol(int strain, Symbol from) {
        super();
        this.strain = strain;
        this.from = from;
    }

    @Override
    public String toString() {
        return Strain.getName(strain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strain);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoundSymbol other = (BoundSymbol) obj;
        return strain == other.strain;
    }

    @Override
    public Integer evaluate(Map<String, Integer> suits) {
        return strain;
    }

    @Override
    public Map<String, Integer> unevaluate(int strain) {
        if (strain != this.strain) {
            throw new IllegalArgumentException();
        }
        return Map.of();
    }

    @Override
    public int getResolved() {
        return strain;
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        return from.compatibleWith(bid);
    }

    @Override
    public List<Symbol> boundSymbols(Map<String, Integer> suits) {
        return List.of(this);
    }
}
