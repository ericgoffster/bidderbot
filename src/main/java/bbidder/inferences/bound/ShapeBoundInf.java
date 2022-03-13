package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
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

    public ShapeBoundInf(ShapeSet shapes) {
        this.shapes = shapes;
    }

    @Override
    public ShapeSet getShapes() {
        return shapes;
    }
    
    @Override
    public ShapeSet getNotShapes() {
        return shapes.not();
    }
    
    @Override
    public Range getHcp() {
        return Range.all(40);
    }
    
    @Override
    public Range getNotHcp() {
        return Range.all(40);
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
