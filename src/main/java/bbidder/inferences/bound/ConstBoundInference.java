package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;

/**
 * Represents a "const" bound inference. Always matches or always does not.
 * 
 * @author goffster
 *
 */
public class ConstBoundInference implements IBoundInference {
    public static ConstBoundInference T = new ConstBoundInference(true);
    public static ConstBoundInference F = new ConstBoundInference(false);
    public final boolean result;
    
    @Override
    public int size() {
        return 1;
    }

    private ConstBoundInference(boolean result) {
        super();
        this.result = result;
    }

    @Override
    public boolean matches(Hand hand) {
        return result;
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
}