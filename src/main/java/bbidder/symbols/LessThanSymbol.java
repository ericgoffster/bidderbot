package bbidder.symbols;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SymbolTable;

public final class LessThanSymbol extends Symbol {
    private final Symbol symbol;
    private final int level;
    private final Symbol other;

    public LessThanSymbol(Symbol symbol, int level, Symbol other) {
        super();
        this.symbol = symbol;
        this.level = level;
        this.other = other;
    }

    @Override
    public String toString() {
        return symbol + ":<" + (level + 1) + other;
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
    public Stream<Context> resolveSymbols(SymbolTable symbols) {
        return symbol.resolveSymbols(symbols)
                .flatMap(e1 -> other.resolveSymbols(e1.symbols)
                        .map(e2 -> new LessThanSymbol(e1.getSymbol(), level, e2.getSymbol()).new Context(e2.symbols)));
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
