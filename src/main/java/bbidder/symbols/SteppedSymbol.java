package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class SteppedSymbol implements Symbol {
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
    public List<Symbol> boundSymbols(SymbolTable suits) {
        List<Symbol> l = new ArrayList<>();
        for(Symbol s: sym.boundSymbols(suits)) {
            l.add(new SteppedSymbol(s, delta));
        }
        return l;
    }

    @Override
    public Symbol evaluate(SymbolTable suits) {
        Symbol s = sym.evaluate(suits);
        if (s == null) {
            return null;
        }
        return new ConstSymbol(transform(s.getResolved()));
    }

    private int transform(Integer s) {
        return (s + 5 - delta) % 5;
    }

    @Override
    public SymbolTable unevaluate(int strain) {
        return sym.unevaluate(untransform(strain));
    }

    private int untransform(int strain) {
        return (strain + delta) % 5;
    }

    @Override
    public int getResolved() {
        return transform(sym.getResolved());
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        return true;
    }
    
    @Override
    public boolean nonConvential() {
        return sym.nonConvential();
    }
}
