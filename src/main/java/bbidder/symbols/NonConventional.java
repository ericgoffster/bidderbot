package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;

public class NonConventional implements Symbol {
    public final Symbol sym;

    public NonConventional(Symbol sym) {
        super();
        this.sym = sym;
    }

    @Override
    public String toString() {
        return sym + ":nonvconventional";
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
        NonConventional other = (NonConventional) obj;
        return Objects.equals(sym, other.sym);
    }

    @Override
    public Symbol evaluate(Map<String, Integer> suits) {
        Symbol evaluate = sym.evaluate(suits);
        if (evaluate == null) {
            return null;
        }
        return new NonConventional(evaluate);
    }

    @Override
    public Map<String, Integer> unevaluate(int strain) {
        return sym.unevaluate(strain);
    }
    
    @Override
    public List<Symbol> boundSymbols(Map<String, Integer> suits) {
        List<Symbol> l = new ArrayList<>();
        for(Symbol s: sym.boundSymbols(suits)) {
            l.add(new NonConventional(s));
        }
        return l;
    }

    @Override
    public int getResolved() {
        return sym.getResolved();
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        return sym.compatibleWith(bid);
    }
    
    @Override
    public boolean nonConvential() {
        return true;
    }
}