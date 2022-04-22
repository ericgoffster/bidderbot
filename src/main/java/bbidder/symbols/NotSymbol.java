package bbidder.symbols;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import bbidder.Symbol;

public class NotSymbol implements Symbol {
    public final Symbol sym;

    public NotSymbol(Symbol sym) {
        super();
        this.sym = sym;
    }

    @Override
    public String toString() {
        return "~" + sym;
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
        NotSymbol other = (NotSymbol) obj;
        return Objects.equals(sym, other.sym);
    }

    @Override
    public short getSuitClass(Map<String, Integer> suits) {
        return (short) (0xf ^ sym.getSuitClass(suits));
    }

    @Override
    public Integer evaluate(Map<String, Integer> suits) {
        return suits.get(toString());
    }

    @Override
    public Map<String, Integer> unevaluate(int strain) {
        return Map.of(toString(), strain);
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }
    
    @Override
    public Comparator<Integer> direction() {
        return sym.direction();
    }
    @Override
    public boolean levelAllowed(int level) {
        return sym.levelAllowed(level);
    }
}
