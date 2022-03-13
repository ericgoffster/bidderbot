package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.ShapeSet;

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
    public ShapeSet getShapes() {
        return result ? ShapeSet.ALL : ShapeSet.NONE;
    } 
    
    @Override
    public ShapeSet getNotShapes() {
        return result ? ShapeSet.NONE : ShapeSet.ALL;
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