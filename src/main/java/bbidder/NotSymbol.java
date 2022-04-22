package bbidder;

import java.util.Map;
import java.util.Objects;

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
    public short getSuitClass() {
        return (short) (0xf ^ sym.getSuitClass());
    }

    @Override
    public Integer evaluate(Map<String, Integer> suits) {
        return suits.get(toString());
    }
    
    @Override
    public void unevaluate(Map<String, Integer> suits, int strain) {
        suits.put(toString(), strain);
    }
    
    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }
}
