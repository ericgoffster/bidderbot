package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bbidder.Bid;
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
    
    public boolean findStrain(int strain, List<Symbol> old) {
        for(Symbol s: old) {
            if (strain == s.getResolved()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<Symbol> boundSymbols(Map<String, Integer> suits) {
        List<Symbol> old = sym.boundSymbols(suits);
        List<Symbol> l = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            int resolved = i;
            if (!findStrain(resolved, old)) {
                l.add(new Symbol() {

                    @Override
                    public Symbol evaluate(Map<String, Integer> suits) {
                        return new ConstSymbol(resolved);
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
                        return bid.strain == resolved;
                    }
                });
            }
        }
        return l;
    }

    @Override
    public Symbol evaluate(Map<String, Integer> suits) {
        if (suits.containsKey(toString())) {
            return new ConstSymbol(suits.get(toString()));
        }
        return null;
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
    public boolean compatibleWith(Bid bid) {
        return !sym.compatibleWith(bid);
    }
}
