package bbidder.inferences;

import java.util.Objects;
import java.util.OptionalInt;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Position;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.utils.MyStream;

public final class Preference extends Inference {
    public static final String NAME = "prefer";
    private final Symbol longer;
    private final Symbol shorter;

    public Preference(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain1 = longer.getResolvedStrain();
        int strain2 = shorter.getResolvedStrain();
        return createrBound(strain1, strain2, players);
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return longer.resolveSuits(suitTable)
                .flatMap(
                        e1 -> shorter.resolveSuits(e1.suitTable).map(e2 -> new Preference(e1.getSymbol(), e2.getSymbol()).new Context(e2.suitTable)));
    }

    @Override
    public String toString() {
        return NAME + " " + longer + " to " + shorter;
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
        Preference other = (Preference) obj;
        return Objects.equals(longer, other.longer) && Objects.equals(shorter, other.shorter);
    }

    public static boolean isBetterFit(Shape shape, int longer, int shorter, int partnerLenLonger, int partnerLenShorter) {
        int diff = partnerLenLonger + shape.numInSuit(longer) - (partnerLenShorter + shape.numInSuit(shorter));
        if (diff > 0) {
            return true;
        }
        if (diff < 0) {
            return false;
        }
        return shape.numInSuit(longer) >= shape.numInSuit(shorter);
    }

    private static IBoundInference createrBound(int longer, int shorter, Players players) {
        Position position = Position.ME;
        OptionalInt minLenInLonger = players.getPlayer(position.getOpposite()).infSummary.minLenInSuit(longer);
        if (!minLenInLonger.isPresent()) {
            return ConstBoundInference.F;
        }
        OptionalInt minLenInShorter = players.getPlayer(position.getOpposite()).infSummary.minLenInSuit(shorter);
        if (!minLenInShorter.isPresent()) {
            return ConstBoundInference.F;
        }
        int partnerLenLonger = minLenInLonger.getAsInt();
        int partnerLenShorter = minLenInShorter.getAsInt();
        return ShapeBoundInf.create(ShapeSet.create(shape -> isBetterFit(shape, longer, shorter, partnerLenLonger, partnerLenShorter)));
    }
}
