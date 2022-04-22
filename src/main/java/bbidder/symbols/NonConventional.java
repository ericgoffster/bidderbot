package bbidder.symbols;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
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
    public Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols) {
        Map<Symbol, SymbolTable> old = symbol.resolveSymbol(symbols);
        Map<Symbol, SymbolTable> m = new LinkedHashMap<>();
        for(var e: old.entrySet()) {
            m.put(new NonConventional(e.getKey()), e.getValue());
        }
        return m;
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
