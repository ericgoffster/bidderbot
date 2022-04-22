package bbidder.inferences.bound;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.StopperSet;

/**
 * Represents the inference for stoppers.
 * 
 * @author goffster
 *
 */
public final class StoppersBoundInf implements IBoundInference {
    private final StopperSet stoppers;

    @Override
    public IBoundInference negate() {
        return create(stoppers.not());
    }

    private StoppersBoundInf(StopperSet stoppers) {
        this.stoppers = stoppers;
    }

    @Override
    public InfSummary getSummary() {
        return InfSummary.ALL.withStoppers(stoppers).withPartialStoppers(stoppers);
    }

    @Override
    public IBoundInference andWith(IBoundInference other) {
        if (other instanceof StoppersBoundInf) {
            return create(((StoppersBoundInf) other).stoppers.and(stoppers));
        }
        return null;
    }

    @Override
    public IBoundInference orWith(IBoundInference other) {
        if (other instanceof StoppersBoundInf) {
            return create(((StoppersBoundInf) other).stoppers.or(stoppers));
        }
        return null;
    }

    public static IBoundInference create(StopperSet r) {
        if (r.isEmpty()) {
            return ConstBoundInference.F;
        }
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        return new StoppersBoundInf(r);
    }

    @Override
    public boolean test(Hand hand) {
        return stoppers.contains(hand.getStoppers());
    }

    @Override
    public String toString() {
        return "stoppers " + stoppers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stoppers);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StoppersBoundInf other = (StoppersBoundInf) obj;
        return Objects.equals(stoppers, other.stoppers);
    }
}
