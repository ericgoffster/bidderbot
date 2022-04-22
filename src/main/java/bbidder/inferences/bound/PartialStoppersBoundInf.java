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
public final class PartialStoppersBoundInf implements IBoundInference {
    final StopperSet stoppers;

    @Override
    public IBoundInference negate() {
        return create(stoppers.not());
    }

    private PartialStoppersBoundInf(StopperSet stoppers) {
        this.stoppers = stoppers;
    }

    @Override
    public InfSummary getSummary() {
        return InfSummary.ALL.withPartialStoppers(stoppers);
    }

    @Override
    public IBoundInference andWith(IBoundInference other) {
        if (other instanceof PartialStoppersBoundInf) {
            return create(((PartialStoppersBoundInf) other).stoppers.and(stoppers));
        }
        return null;
    }

    @Override
    public IBoundInference orWith(IBoundInference other) {
        if (other instanceof PartialStoppersBoundInf) {
            return create(((PartialStoppersBoundInf) other).stoppers.or(stoppers));
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
        return new PartialStoppersBoundInf(r);
    }

    @Override
    public boolean test(Hand hand) {
        return stoppers.contains(hand.getPartialStoppers());
    }

    @Override
    public String toString() {
        return "partial stoppers " + stoppers;
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
        PartialStoppersBoundInf other = (PartialStoppersBoundInf) obj;
        return Objects.equals(stoppers, other.stoppers);
    }
}
