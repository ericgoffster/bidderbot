package bbidder.inferences;

import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.StopperSet;
import bbidder.SuitSet;
import bbidder.SuitTable;
import bbidder.inferences.bound.PartialStoppersBoundInf;
import bbidder.inferences.bound.StoppersBoundInf;
import bbidder.utils.BitUtil;
import bbidder.utils.MyStream;

/**
 * Represents the inference of the stoppers in a suit set.
 * 
 * @author goffster
 *
 */
public final class StoppersInSuits extends Inference {
    public static final String FULL = "stoppers";
    public static final String PARTIAL = "partial_stoppers";
    private final SuitSet suits;
    private final boolean partial;

    public StoppersInSuits(SuitSet suits, boolean partial) {
        super();
        this.suits = suits;
        this.partial = partial;
    }

    @Override
    public IBoundInference bind(Players players) {
        short theSuits = suits.evaluate(players);
        StopperSet stoppers = new StopperSet(stopper -> !BitUtil.stream(theSuits).filter(s -> !stopper.stopperIn(s)).findFirst().isPresent());
        if (partial) {
            return PartialStoppersBoundInf.create(stoppers);
        }
        return StoppersBoundInf.create(stoppers);
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return suits.resolveSuits(suitTable).map(e -> new StoppersInSuits(e.getSuitSet(), partial).new Context(e.suitTable));
    }

    @Override
    public String toString() {
        return (partial ? PARTIAL : FULL) + " " + suits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(partial, suits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StoppersInSuits other = (StoppersInSuits) obj;
        return partial == other.partial && Objects.equals(suits, other.suits);
    }
}
