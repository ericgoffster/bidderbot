package bbidder.symbols;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import bbidder.Bid;
import bbidder.Symbol;

public class SteppedSymbol implements Symbol {
    public final Symbol sym;
    public final int delta;

    public SteppedSymbol(Symbol sym, int delta) {
        super();
        this.sym = sym;
        this.delta = delta;
    }

    @Override
    public String toString() {
        return sym + "-" + delta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(delta, sym);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SteppedSymbol other = (SteppedSymbol) obj;
        return delta == other.delta && Objects.equals(sym, other.sym);
    }
    
    @Override
    public List<Symbol> boundSymbols(Map<String, Integer> suits) {
        List<Symbol> l = new ArrayList<>();
        for(Symbol s: sym.boundSymbols(suits)) {
            l.add(new BoundSymbol(transform(s.getResolved()), this));
        }
        return l;
    }

    @Override
    public Integer evaluate(Map<String, Integer> suits) {
        Integer s = sym.evaluate(suits);
        if (s == null) {
            return null;
        }
        return transform(s);
    }

    private int transform(Integer s) {
        return (s + 5 - delta) % 5;
    }

    @Override
    public Map<String, Integer> unevaluate(int strain) {
        return sym.unevaluate(untransform(strain));
    }

    private int untransform(int strain) {
        return (strain + delta) % 5;
    }

    /**
     * 
     * @param suits
     *            The suits to rotate
     * @return The suits, rotated down once.
     */
    public static short rotateDown(short suits) {
        return (short) ((suits >> 1) | ((suits & 1) << 4));
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
        return sym.levelTest();
    }
}
