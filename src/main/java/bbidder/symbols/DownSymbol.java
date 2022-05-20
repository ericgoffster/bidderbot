package bbidder.symbols;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import bbidder.Bid;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.IteratorStream;
import bbidder.utils.MyStream;

public final class DownSymbol extends Symbol {
    public static final String NAME = "down";
    private final Symbol symbol;

    public DownSymbol(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public boolean downTheLine() {
        return true;
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
        DownSymbol other = (DownSymbol) obj;
        return Objects.equals(symbol, other.symbol);
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
        return symbol.isNonConvential();
    }

    @Override
    public short getSeats() {
        return symbol.getSeats();
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        List<Context> l = new ArrayList<>();
        symbol.resolveSuits(suitTable).forEach(l::add);
        Collections.reverse(l);
        return new IteratorStream<Symbol.Context>(l.iterator());
    }
}
