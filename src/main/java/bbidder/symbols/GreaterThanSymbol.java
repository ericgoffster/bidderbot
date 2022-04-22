package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class GreaterThanSymbol implements Symbol {
    public final Symbol symbol;
    public final int level;
    public final Symbol other;

    public GreaterThanSymbol(Symbol symbol, int level, Symbol other) {
        super();
        this.symbol = symbol;
        this.level = level;
        this.other = other;
    }

    @Override
    public String toString() {
        return symbol + ":>"+(level + 1)+other;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, symbol, other);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GreaterThanSymbol other = (GreaterThanSymbol) obj;
        return level == other.level && Objects.equals(symbol, other.symbol) && Objects.equals(other, other.other);
    }

    @Override
    public Symbol evaluate(SymbolTable symbols) {
        Symbol evaluate = symbol.evaluate(symbols);
        if (evaluate == null) {
            return null;
        }
        return new GreaterThanSymbol(evaluate, level, other);
    }

    @Override
    public SymbolTable unevaluate(int strain) {
        return symbol.unevaluate(strain);
    }
    
    @Override
    public List<Symbol> boundSymbols(SymbolTable symbols) {
        List<Symbol> boundOthers = other.boundSymbols(symbols);
        if (boundOthers.size() != 1) {
            throw new IllegalArgumentException("undefined smybol: " + other);
        }
        List<Symbol> old = symbol.boundSymbols(symbols);
        List<Symbol> l = new ArrayList<>();
        for(Symbol s: old) {
            l.add(new GreaterThanSymbol(s, level, boundOthers.get(0)));
        }
        return l;
    }

    @Override
    public int getResolved() {
        return symbol.getResolved();
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        Bid comparisonBid = Bid.valueOf(level, other.getResolved());
        return symbol.compatibleWith(bid) && bid.compareTo(comparisonBid) > 0;
    }
    
    @Override
    public boolean nonConvential() {
        return symbol.nonConvential();
    }
}
