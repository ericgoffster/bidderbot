package bbidder.symbols;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;

public class LessThanSymbol implements Symbol {
    public final Symbol sym;
    public final int level;

    public LessThanSymbol(Symbol sym, int level) {
        super();
        this.sym = sym;
        this.level = level;
    }

    @Override
    public String toString() {
        return sym + ":<"+(level + 1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, sym);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LessThanSymbol other = (LessThanSymbol) obj;
        return level == other.level && Objects.equals(sym, other.sym);
    }

    @Override
    public Integer evaluate(Map<String, Integer> suits) {
        return sym.evaluate(suits);
    }

    @Override
    public Map<String, Integer> unevaluate(int strain) {
        return sym.unevaluate(strain);
    }

    @Override
    public List<Symbol> boundSymbols(Map<String, Integer> suits) {
        int resolved = sym.getResolved();
        return List.of(new Symbol() {

            @Override
            public Integer evaluate(Map<String, Integer> suits) {
                return resolved;
            }

            @Override
            public Map<String, Integer> unevaluate(int strain) {
                return Map.of();
            }

            @Override
            public List<Symbol> boundSymbols(Map<String, Integer> suits) {
                return List.of(this);
            }

            @Override
            public int getResolved() {
                return resolved;
            }

            @Override
            public boolean compatibleWith(Bid bid) {
                return LessThanSymbol.this.compatibleWith(bid);
            }
            
        });
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        return level > bid.level;
    }
}
