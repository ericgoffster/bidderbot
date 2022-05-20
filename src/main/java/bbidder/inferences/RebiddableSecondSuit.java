package bbidder.inferences;

import java.util.Objects;
import java.util.OptionalInt;

import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Position;
import bbidder.ShapeSet;
import bbidder.SuitLengthRange;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.utils.MyStream;

public final class RebiddableSecondSuit extends Inference {
    public static final String NAME = "rebiddable_2nd";
    private final Symbol longer;
    private final Symbol shorter;

    public RebiddableSecondSuit(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public IBoundInference bind(Players players) {
        Position position = Position.ME;
        int strainLonger = longer.getResolvedStrain();
        int strainShorter = shorter.getResolvedStrain();
        return createrBound(strainLonger, strainShorter, players.getPlayer(position).infSummary);
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return longer.resolveSuits(suitTable)
                .flatMap(e1 -> shorter.resolveSuits(e1.suitTable)
                        .map(e2 -> new RebiddableSecondSuit(e1.getSymbol(), e2.getSymbol()).new Context(e2.suitTable)));
    }

    private static IBoundInference createrBound(int longer, int shorter, InfSummary meSummary) {
        OptionalInt bidSuits = meSummary.getBidSuits();
        if (!bidSuits.isPresent()) {
            return ConstBoundInference.F;
        }
        if ((bidSuits.getAsInt() & (1 << longer)) == 0) {
            return ConstBoundInference.F;
        }
        if ((bidSuits.getAsInt() & (1 << shorter)) == 0) {
            return ConstBoundInference.F;
        }
        OptionalInt minLenInSuit = meSummary.minLenInSuit(shorter);
        if (!minLenInSuit.isPresent()) {
            return ConstBoundInference.F;
        }
        int myMinLen = minLenInSuit.getAsInt();
        SuitLengthRange r = SuitLengthRange.atLeast(Math.max(myMinLen + 1, 5));
        return ShapeBoundInf
                .create(ShapeSet.create(shape -> shape.isSuitInRange(shorter, r) && shape.isLongerOrEqual(longer, (short) (1 << shorter))));
    }

    @Override
    public String toString() {
        return NAME + " " + longer + " " + shorter;
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
