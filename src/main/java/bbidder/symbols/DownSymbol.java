package bbidder.symbols;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import bbidder.Bid;
import bbidder.Symbol;

public class DownSymbol implements Symbol {
    public final Symbol sym;

    public DownSymbol(Symbol sym) {
        super();
        this.sym = sym;
    }

    @Override
    public String toString() {
        return sym + ":down";
    }

    @Override
    public int hashCode() {
        return Objects.hash(sym);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DownSymbol other = (DownSymbol) obj;
        return Objects.equals(sym, other.sym);
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
    public short getSuitClass(Map<String, Integer> suits) {
        return sym.getSuitClass(suits);
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }
    
    @Override
    public Comparator<Symbol> direction() {
        return sym.direction().reversed();
    }

    @Override
    public Predicate<Bid> levelTest() {
        return sym.levelTest();
    }
}
