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

public final class RebiddableSecondSuit extends Inference {
    private final Symbol longer;
    private final Symbol shorter;

    public static Pattern PATT_FIT = Pattern.compile("\\s*rebiddable_2nd\\s+(.*)\\s+(.*)", Pattern.CASE_INSENSITIVE);

    public RebiddableSecondSuit(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strainLonger = longer.getResolvedStrain();
        int strainShorter = shorter.getResolvedStrain();
        return createrBound(strainLonger, strainShorter, players.me.infSummary);
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return longer.resolveSuits(suitTable)
                .flatMap(e1 -> shorter.resolveSuits(e1.suitTable)
                        .map(e2 -> new RebiddableSecondSuit(e1.getSymbol(), e2.getSymbol()).new Context(e2.suitTable)));
    }

    private static IBoundInference createrBound(int longer, int shorter, InfSummary meSummary) {
        OptionalInt minLenInSuit = meSummary.minLenInSuit(shorter);
        if (!minLenInSuit.isPresent()) {
            return ConstBoundInference.F;
        }
        int myMinLen = minLenInSuit.getAsInt();
        SuitLengthRange r = SuitLengthRange.atLeast(Math.max(myMinLen + 1, 5));
        return ShapeBoundInf.create(
                ShapeSet.create(shape -> shape.isSuitInRange(shorter, r) && shape.isLongerOrEqual(longer, (short)(1 << shorter))));
    }

    @Override
    public String toString() {
        return "rebiddable_2nd " + longer + " " + shorter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longer, shorter);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RebiddableSecondSuit other = (RebiddableSecondSuit) obj;
        return Objects.equals(longer, other.longer) && Objects.equals(shorter, other.shorter);
    }

}
