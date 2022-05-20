package bbidder.generalities;

import java.util.Objects;
import java.util.OptionalInt;

import bbidder.Auction;
import bbidder.Generality;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.utils.MyStream;

public final class FitEstablished extends Generality {
    public static final String NAME = "fit_established";
    private final Symbol symbol;
    private final int combined;

    public FitEstablished(Symbol symbol, int combined) {
        super();
        this.symbol = symbol;
        this.combined = combined;
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new FitEstablished(e.getSymbol(), combined).new Context(e.suitTable));
    }

    @Override
    public boolean test(Players players, Auction bidList) {
        int s = symbol.getResolvedStrain();
        OptionalInt ourCombinedMinLength = players.ourCombinedMinLength(s);
        if (!ourCombinedMinLength.isPresent()) {
            return false;
        }
        return ourCombinedMinLength.getAsInt() >= combined;
    }

    @Override
    public int hashCode() {
        return Objects.hash(combined, symbol);
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
        return combined == other.combined && Objects.equals(symbol, other.symbol);
    }

    @Override
    public String toString() {
        return "fit" + combined + "_established" + " " + symbol;
    }

}
