package bbidder.symbols;

import java.util.Objects;
import java.util.Set;

import bbidder.Bid;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class NonConventional extends Symbol {
    public static final String NAME = "nonconventional";
    private final Symbol symbol;

    public NonConventional(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public boolean downTheLine() {
        return false;
    }

    @Override
    public Set<String> getTags() {
        return symbol.getTags();
    }

    @Override
    public String toString() {
        return symbol + ":" + NAME;
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
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new NonConventional(e.getSymbol()).new Context(e.suitTable));
    }

    @Override
    public int getResolvedStrain() {
        return symbol.getResolvedStrain();
    }

    @Override
    public boolean compatibleWith(Bid bid) {
        return symbol.compatibleWith(bid);
    }

    @Override
    public boolean isNonConvential() {
        return true;
    }

    @Override
    public short getSeats() {
        return 0xf;
    }
}
