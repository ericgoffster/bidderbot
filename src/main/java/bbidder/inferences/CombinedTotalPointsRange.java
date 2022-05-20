package bbidder.inferences;

import java.util.Objects;
import java.util.OptionalInt;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.PointRange;
import bbidder.Position;
import bbidder.SuitTable;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.TotalPtsBoundInf;
import bbidder.utils.MyStream;

/**
 * Represents the inference of the total points in a suit.
 * 
 * @author goffster
 *
 */
public final class CombinedTotalPointsRange extends Inference {
    public final PointRange rng;

    public CombinedTotalPointsRange(Integer min, Integer max) {
        super();
        this.rng = PointRange.between(min, max);
    }

    public CombinedTotalPointsRange(PointRange rng) {
        super();
        this.rng = rng;
    }

    @Override
    public IBoundInference bind(Players players) {
        Position position = Position.ME;
        OptionalInt minTotalPts = players.getPlayer(position.getOpposite()).infSummary.minTotalPts();
        if (!minTotalPts.isPresent()) {
            return ConstBoundInference.F;
        }
        int tpts = minTotalPts.getAsInt();
        return TotalPtsBoundInf.create(players.getPlayer(position.getOpposite()).infSummary, new PointRange(rng.bits >> tpts));
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return rng + " combined tpts";
    }

    @Override
    public int hashCode() {
        return Objects.hash(rng);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CombinedTotalPointsRange other = (CombinedTotalPointsRange) obj;
        return Objects.equals(rng, other.rng);
    }
}
