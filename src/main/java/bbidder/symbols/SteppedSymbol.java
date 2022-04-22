package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolContext;
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
    public List<SymbolContext> resolveSymbol(SymbolTable symbols) {
        List<SymbolContext> l = new ArrayList<>();
        for(var e: symbol.resolveSymbol(symbols)) {
            l.add(new SymbolContext(new SteppedSymbol(e.symbol, delta), e.symbols));
        }
        return l;
    }

    private int transform(Integer s) {
        return (s + 5 - delta) % 5;
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
