package bbidder.inferences;

import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.PointRange;
import bbidder.Position;
import bbidder.SuitTable;
import bbidder.inferences.bound.TotalPtsBoundInf;
import bbidder.utils.MyStream;

/**
 * Represents the inference of the total points in a suit.
 * 
 * @author goffster
 *
 */
public final class TotalPointsRange extends Inference {
    public static final String TPTS = "tpts";
    private final PointRange rng;

    public TotalPointsRange(Integer min, Integer max) {
        super();
        this.rng = PointRange.between(min, max);
    }

    public TotalPointsRange(PointRange rng) {
        super();
        this.rng = rng;
    }

    @Override
    public IBoundInference bind(Players players) {
        Position position = Position.ME;
        return TotalPtsBoundInf.create(players.getPlayer(position.getOpposite()).infSummary, rng);
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return rng + " " + TPTS;
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
        TotalPointsRange other = (TotalPointsRange) obj;
        return Objects.equals(rng, other.rng);
    }
}
