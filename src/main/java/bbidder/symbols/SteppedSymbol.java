package bbidder.symbols;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class SteppedSymbol implements Symbol {
    public final Symbol symbol;
    public final int delta;

    public SteppedSymbol(Symbol sym, int delta) {
        super();
        this.symbol = sym;
        this.delta = delta;
    }

    @Override
    public String toString() {
        return symbol + "-" + delta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(delta, symbol);
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
        return delta == other.delta && Objects.equals(symbol, other.symbol);
    }
    
    @Override
    public List<Symbol> boundSymbols(SymbolTable symbols) {
        List<Symbol> l = new ArrayList<>();
        for(Symbol s: symbol.boundSymbols(symbols)) {
            l.add(new SteppedSymbol(s, delta));
        }
        return l;
    }
    
    @Override
    public Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols) {
        Map<Symbol, SymbolTable> old = symbol.resolveSymbol(symbols);
        Map<Symbol, SymbolTable> m = new LinkedHashMap<>();
        for(var e: old.entrySet()) {
            m.put(new SteppedSymbol(e.getKey(), delta), e.getValue());
        }
        return m;
    }

    @Override
    public Symbol evaluate(SymbolTable symbols) {
        Symbol s = symbol.evaluate(symbols);
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
        return symbol.unevaluate(untransform(strain));
    }

    private int untransform(int strain) {
        return (strain + delta) % 5;
    }

    @Override
    public int getResolved() {
        return transform(symbol.getResolved());
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        return true;
    }
    
    @Override
    public boolean nonConvential() {
        return symbol.nonConvential();
    }
}
