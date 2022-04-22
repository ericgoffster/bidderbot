package bbidder.symbols;

import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.ListUtil;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class GreaterThanSymbol extends Symbol {
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
    public List<SymbolContext> resolveSymbols(SymbolTable symbols) {
        return ListUtil.flatMap(symbol.resolveSymbols(symbols), e1 -> ListUtil.map(other.resolveSymbols(e1.symbols),
                e2 -> new SymbolContext(new GreaterThanSymbol(e1.getSymbol(), level, e2.getSymbol()), e2.symbols)));
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
