package bbidder.symbols;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bbidder.Bid;
import bbidder.Symbol;
import bbidder.SuitTable;

public final class DownSymbol extends Symbol {
    private final Symbol symbol;

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

    @Override
    public Stream<Context> resolveSymbols(SuitTable suitTable) {
        List<Context> l = new ArrayList<>(symbol.resolveSymbols(suitTable).collect(Collectors.toList()));
        Collections.reverse(l);
        return l.stream();
    }
}
