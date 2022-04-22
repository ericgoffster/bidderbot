package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public List<Symbol> boundSymbols(Map<String, Integer> suits) {
        List<Symbol> l = new ArrayList<>();
        for(Symbol s: sym.boundSymbols(suits)) {
            l.add(0, s);
        }
        return l;
    }

    @Override
    public int getResolved() {
        throw new IllegalStateException(this + " not resolved");
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        return sym.compatibleWith(bid);
    }
}
