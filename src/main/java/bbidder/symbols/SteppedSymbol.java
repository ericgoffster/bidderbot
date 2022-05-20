package bbidder.symbols;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SuitTable;

public final class SteppedSymbol extends Symbol {
    private final Symbol symbol;
    private final int delta;

    public SteppedSymbol(Symbol sym, int delta) {
        super();
        this.symbol = sym;
        this.delta = delta;
    }

    @Override
    public String toString() {
        return symbol + "-" + delta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(delta, symbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SteppedSymbol other = (SteppedSymbol) obj;
        return delta == other.delta && Objects.equals(symbol, other.symbol);
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new SteppedSymbol(e.getSymbol(), delta).new Context(e.suitTable));
    }

    private int transform(Integer s) {
        return (s + 5 - delta) % 5;
    }

    @Override
    public int getResolvedStrain() {
        return transform(symbol.getResolvedStrain());
    }

    @Override
    public boolean compatibleWith(Bid bid) {
        return true;
    }

    @Override
    public boolean isNonConvential() {
        return symbol.isNonConvential();
    }

    @Override
    public short getSeats() {
        return symbol.getSeats();
    }
}
