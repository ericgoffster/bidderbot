package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;

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
    public InfSummary getSummary() {
        return result ? InfSummary.ALL : InfSummary.NONE;
    }
    
    @Override
    public IBoundInference andWith(IBoundInference other) {
        return result ? other : F;
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
    public boolean matches(Hand hand) {
        return result;
    }

    public static IBoundInference create(boolean result) {
        return result ? T : F;
    }

    @Override
    public String toString() {
        return String.valueOf(result);
    }
}