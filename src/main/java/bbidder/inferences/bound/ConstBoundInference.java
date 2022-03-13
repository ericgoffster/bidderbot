package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Range;
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
    
    @Override
    public Range getHcp() {
        return result ? Range.all(40) : Range.none(40);
    }
    
    @Override
    public Range getNotHcp() {
        return result ? Range.none(40) : Range.all(40);
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