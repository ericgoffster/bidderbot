package bbidder.suitsets;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.Symbol;

public final class LookupSet extends SuitSet {
    private final Symbol symbol;

    public LookupSet(Symbol strain) {
        this.symbol = strain;
    }

    @Override
    public short evaluate(Players players) {
        int st = symbol.getResolvedStrain();
        return (short) (1 << st);
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
        LookupSet other = (LookupSet) obj;
        return Objects.equals(symbol, other.symbol);
    }

    @Override
    public String toString() {
        return symbol.toString();
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new LookupSet(e.getSymbol()).new Context(e.suitTable));
    }
}