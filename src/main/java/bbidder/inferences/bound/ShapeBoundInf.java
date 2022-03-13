package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Shape;
import bbidder.ShapeSet;

/**
 * Represents the inference for a high card point range.
 * 
 * @author goffster
 *
 */
public class ShapeBoundInf implements IBoundInference {
    final ShapeSet r;

    public ShapeBoundInf(ShapeSet r) {
        this.r = r;
    }
    
    @Override
    public int size() {
        return 1;
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
        Shape s = Shape.getShape(hand.numInSuit(0), hand.numInSuit(1), hand.numInSuit(2), hand.numInSuit(3));
        return r.contains(s);
    }

    @Override
    public IBoundInference negate() {
        return new ShapeBoundInf(r.not());
    }

    @Override
    public String toString() {
        return r.toString();
    }

    @Override
    public IBoundInference andReduce(IBoundInference i) {
        if (i instanceof ShapeBoundInf) {
            return create(r.and(((ShapeBoundInf) i).r));
        }
        return null;
    }

    @Override
    public IBoundInference orReduce(IBoundInference i) {
        if (i instanceof ShapeBoundInf) {
            return create(r.or(((ShapeBoundInf) i).r));
        }
        return null;
    }
}
