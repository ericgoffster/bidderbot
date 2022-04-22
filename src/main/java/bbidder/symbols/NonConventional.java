package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolContext;
import bbidder.SymbolTable;

public final class NonConventional implements Symbol {
    public final Symbol symbol;

    public NonConventional(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol + ":nonvconventional";
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
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
        return Objects.equals(symbol, other.symbol);
    }

    @Override
    public List<SymbolContext> resolveSymbol(SymbolTable symbols) {
        List<SymbolContext> l = new ArrayList<>();
        for(var e: symbol.resolveSymbol(symbols)) {
            l.add(new SymbolContext(new NonConventional(e.symbol), e.symbols));
        }
        return l;
    }

    @Override
    public int getResolved() {
        return symbol.getResolved();
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        return symbol.compatibleWith(bid);
    }
    
    @Override
    public boolean nonConvential() {
        return true;
    }
}
