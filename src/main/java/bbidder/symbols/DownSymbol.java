package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
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
    public Symbol evaluate(SymbolTable symbols) {
        Symbol evaluate = symbol.evaluate(symbols);
        if (evaluate == null) {
            return null;
        }
        return new DownSymbol(evaluate);
    }

    @Override
    public SymbolTable unevaluate(int strain) {
        return symbol.unevaluate(strain);
    }
    
    @Override
    public List<Symbol> boundSymbols(SymbolTable symbols) {
        List<Symbol> l = new ArrayList<>();
        for(Symbol s: symbol.boundSymbols(symbols)) {
            l.add(0, s);
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
        return symbol.nonConvential();
    }
}
