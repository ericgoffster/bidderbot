package bbidder.suitsets;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.Players;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.Symbol;

public final class Gt extends SuitSet {
    private final Symbol symbol;

    public Gt(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public short evaluate(Players players) {
        Integer st = symbol.getResolvedStrain();
        return (short) (0xf & ~((1 << (st + 1)) - 1));
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
        Gt other = (Gt) obj;
        return Objects.equals(symbol, other.symbol);
    }

    @Override
    public String toString() {
        return ">" + symbol;
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new Gt(e.getSymbol()).new Context(suitTable));
    }
}