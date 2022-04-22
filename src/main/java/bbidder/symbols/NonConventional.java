package bbidder.symbols;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SuitTable;

public final class NonConventional extends Symbol {
    private final Symbol symbol;

    public NonConventional(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol + ":nonconventional";
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
    public Stream<Context> resolveSymbols(SuitTable suitTable) {
        return symbol.resolveSymbols(suitTable).map(e -> new NonConventional(e.getSymbol()).new Context(e.suitTable));
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
