package bbidder.inferences;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;

public class ConstBoundInference implements IBoundInference {
    public static ConstBoundInference T = new ConstBoundInference(true);
    public static ConstBoundInference F = new ConstBoundInference(false);
    public final boolean result;

    private ConstBoundInference(boolean result) {
        super();
        this.result = result;
    }

    public boolean matches(Hand hand) {
        return result;
    }

    @Override
    public boolean negatable() {
        return true;
    }

    @Override
    public IBoundInference negate() {
        return create(!result);
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
