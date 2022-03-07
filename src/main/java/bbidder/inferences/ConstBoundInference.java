package bbidder.inferences;

import bbidder.Hand;
import bbidder.IBoundInference;

public class ConstBoundInference implements IBoundInference {
    public final boolean result;

    private ConstBoundInference(boolean result) {
        super();
        this.result = result;
    }

    public boolean matches(Hand hand) {
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(result);
    }

    public static ConstBoundInference T = new ConstBoundInference(true);
    public static ConstBoundInference F = new ConstBoundInference(false);

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
}
