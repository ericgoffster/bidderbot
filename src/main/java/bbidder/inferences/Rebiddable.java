package bbidder.inferences;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Inference;
import bbidder.Players;
import bbidder.ShapeSet;
import bbidder.SuitLengthRange;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;

public final class Rebiddable extends Inference {
    private final Symbol symbol;

    public static Pattern PATT_FIT = Pattern.compile("\\s*rebiddable\\s*(.*)", Pattern.CASE_INSENSITIVE);

    public Rebiddable(Symbol suit) {
        super();
        this.symbol = suit;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = symbol.getResolvedStrain();
        return createrBound(strain, players.me.infSummary);
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new Rebiddable(e.getSymbol()).new Context(e.suitTable));
    }

    private IBoundInference createrBound(int suit, InfSummary meSummary) {
        OptionalInt minLenInSuit = meSummary.minLenInSuit(suit);
        if (!minLenInSuit.isPresent()) {
            return ConstBoundInference.F;
        }
        int myMinLen = minLenInSuit.getAsInt();
        SuitLengthRange r = SuitLengthRange.atLeast(Math.max(myMinLen + 1, 6));
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(suit, r)));
    }

    @Override
    public String toString() {
        return "rebiddable " + symbol;
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
        Rebiddable other = (Rebiddable) obj;
        return Objects.equals(symbol, other.symbol);
    }
}
