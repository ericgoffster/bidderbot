package bbidder;

import java.util.Map;
import java.util.Objects;

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
    public void unevaluate(Map<String, Integer> suits, int strain) {
        sym.unevaluate(suits, strain);
    }

    @Override
    public short getSuitClass() {
        return sym.getSuitClass();
    }
    
    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }
}
