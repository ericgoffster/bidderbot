package bbidder.generalities;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;

public final class FitEstablished extends Generality {
    public static final String NAME = "fit_established";
    private final Symbol symbol;

    public FitEstablished(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new FitEstablished(e.getSymbol()).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int s = symbol.getResolvedStrain();
        OptionalInt ourCombinedMinLength = players.ourCombinedMinLength(s);
        if (!ourCombinedMinLength.isPresent()) {
            return false;
        }
        return ourCombinedMinLength.getAsInt() >= 8;
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
        FitEstablished other = (FitEstablished) obj;
        return Objects.equals(symbol, other.symbol);
    }

    @Override
    public String toString() {
        return NAME + " " + symbol;
    }

}
