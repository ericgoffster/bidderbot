package bbidder.inferences.bound;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;

/**
 * Represents a "const" bound inference. Always matches or always does not.
 * 
 * @author goffster
 *
 */
public final class ConstBoundInference implements IBoundInference {
    public static ConstBoundInference T = new ConstBoundInference(true);
    public static ConstBoundInference F = new ConstBoundInference(false);
    public final boolean result;

    @Override
    public InfSummary getSummary() {
        return result ? InfSummary.ALL : InfSummary.NONE;
    }

    @Override
    public IBoundInference andWith(IBoundInference other) {
        return null;
    }

    @Override
    public IBoundInference orWith(IBoundInference other) {
        return null;
    }

    @Override
    public IBoundInference negate() {
        return result ? F : T;
    }

    private ConstBoundInference(boolean result) {
        super();
        this.result = result;
    }

    @Override
    public boolean test(Hand hand) {
        return result;
    }

    public static IBoundInference create(boolean result) {
        return result ? T : F;
    }

    @Override
    public String toString() {
        return String.valueOf(result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConstBoundInference other = (ConstBoundInference) obj;
        return result == other.result;
    }
}