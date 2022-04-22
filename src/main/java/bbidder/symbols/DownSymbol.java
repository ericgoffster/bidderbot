package bbidder.symbols;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class DownSymbol implements Symbol {
    public final Symbol symbol;

    public DownSymbol(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol + ":down";
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
        DownSymbol other = (DownSymbol) obj;
        return Objects.equals(symbol, other.symbol);
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
        return symbol.nonConvential();
    }
    
    @Override
    public Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols) {
        ArrayList<Entry<Symbol, SymbolTable>> l = new ArrayList<>(symbol.resolveSymbol(symbols).entrySet());
        Collections.reverse(l);
        Map<Symbol, SymbolTable> m = new LinkedHashMap<>();
        for(Entry<Symbol, SymbolTable> e: l) {
            m.put(e.getKey(), e.getValue());
        }
        return m;
    }
}
