package bbidder;

import java.util.Map;
import java.util.Objects;

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
    public short getSuitClass() {
        int n = delta;
        short suits = sym.getSuitClass();
        while (n > 0) {
            suits = rotateDown(suits);
            n--;
        }
        return suits;
    }
    
    @Override
    public Integer evaluate(Map<String, Integer> suits) {
        Integer s = sym.evaluate(suits);
        if (s == null) {
            return null;
        }
        return (s + 5 - delta) % 5;
    }
    
    @Override
    public void unevaluate(Map<String, Integer> suits, int strain) {
        sym.unevaluate(suits, (strain + delta) % 5);
    }

    /**
     * 
     * @param suits The suits to rotate
     * @return The suits, rotated down once.
     */
    public static short rotateDown(short suits) {
        return (short) ((suits >> 1) | ((suits & 1) << 4));
    }
    
    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }
}
