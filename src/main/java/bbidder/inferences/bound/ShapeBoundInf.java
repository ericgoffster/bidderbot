package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.ShapeSet;

/**
 * Represents the inference for a high card point range.
 * 
 * @author goffster
 *
 */
public class ShapeBoundInf implements IBoundInference {
    final ShapeSet shapes;

    @Override
    public IBoundInference negate() {
        return create(shapes.not());
    }  

    private ShapeBoundInf(ShapeSet shapes) {
        this.shapes = shapes;
    }

    @Override
    public InfSummary getSummary() {
        return new InfSummary(shapes, Range.all(40));
    }
    
    @Override
    public IBoundInference andWith(IBoundInference other) {
        if (other instanceof ShapeBoundInf) {
            return create(((ShapeBoundInf)other).shapes.and(shapes));
        }
        return null;
    }
    
    
    public static IBoundInference create(ShapeSet r) {
        if (r.isEmpty()) {
            return ConstBoundInference.F;
        }
        if (r.isFull()) {
            return ConstBoundInference.T;
        }
        return new ShapeBoundInf(r);
    }

    @Override
    public boolean matches(Hand hand) {
        return shapes.contains(hand.getShape());
    }

    @Override
    public String toString() {
        return shapes.toString();
    }
}
