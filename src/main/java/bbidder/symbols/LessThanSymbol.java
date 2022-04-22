package bbidder.symbols;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class LessThanSymbol implements Symbol {
    public final Symbol symbol;
    public final int level;
    public final Symbol other;

    public LessThanSymbol(Symbol symbol, int level, Symbol other) {
        super();
        this.symbol = symbol;
        this.level = level;
        this.other = other;
    }

    @Override
    public String toString() {
        return symbol + ":<"+(level + 1)+other;
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
        LessThanSymbol other = (LessThanSymbol) obj;
        return level == other.level && Objects.equals(symbol, other.symbol) && Objects.equals(other, other.other);
    }

    @Override
    public Symbol evaluate(SymbolTable symbols) {
        Symbol evaluate = symbol.evaluate(symbols);
        if (evaluate == null) {
            return null;
        }
        return new LessThanSymbol(evaluate, level, other);
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
            l.add(new LessThanSymbol(s, level, boundOthers.get(0)));
        }
        return l;
    }

    @Override
    public Map<Symbol, SymbolTable> resolveSymbol(SymbolTable symbols) {
        Map<Symbol, SymbolTable> boundOthers = other.resolveSymbol(symbols);
        if (boundOthers.size() != 1) {
            throw new IllegalArgumentException("undefined smybol: " + other);
        }
        Symbol bo = boundOthers.keySet().iterator().next();
        Map<Symbol, SymbolTable> old = symbol.resolveSymbol(symbols);
        Map<Symbol, SymbolTable> m = new LinkedHashMap<>();
        for(var e: old.entrySet()) {
            m.put(new LessThanSymbol(e.getKey(), level, bo), e.getValue());
        }
        return m;
    }

    @Override
    public int getResolved() {
        return symbol.getResolved();
    }
    
    @Override
    public boolean compatibleWith(Bid bid) {
        Bid comparisonBid = Bid.valueOf(level, other.getResolved());
        return symbol.compatibleWith(bid) && bid.compareTo(comparisonBid) < 0;
    }
    
    @Override
    public boolean nonConvential() {
        return symbol.nonConvential();
    }
}
