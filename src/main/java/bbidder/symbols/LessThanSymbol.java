package bbidder.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolContext;
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
    public List<SymbolContext> resolveSymbols(SymbolTable symbols) {
        List<SymbolContext> l = new ArrayList<>();
        for(var e1: symbol.resolveSymbols(symbols)) {
            for(var e2: other.resolveSymbols(e1.symbols)) {
                l.add(new SymbolContext(new LessThanSymbol(e1.symbol, level, e2.symbol), e2.symbols));
            }
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
        return symbol.compatibleWith(bid) && bid.compareTo(comparisonBid) < 0;
    }
    
    @Override
    public boolean nonConvential() {
        return symbol.nonConvential();
    }
}
