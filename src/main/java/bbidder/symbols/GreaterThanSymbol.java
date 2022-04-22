package bbidder.symbols;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import bbidder.Bid;
import bbidder.Symbol;

public class GreaterThanSymbol implements Symbol {
    public final Symbol sym;
    public final int level;

    public GreaterThanSymbol(Symbol sym, int level) {
        super();
        this.sym = sym;
        this.level = level;
    }

    @Override
    public String toString() {
        return sym + ":>"+level;
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
        GreaterThanSymbol other = (GreaterThanSymbol) obj;
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
        return List.of(new BoundSymbol(sym.getResolved(), this));
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }
    
    @Override
    public Comparator<Symbol> direction() {
        return sym.direction();
    }
    
    @Override
    public Predicate<Bid> levelTest() {
        return lev -> level < lev.level;
    }
}
