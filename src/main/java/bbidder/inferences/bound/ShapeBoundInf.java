package bbidder.inferences.bound;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.ShapeSet;

/**
 * Represents the inference for a high card point range.
 * 
 * @author goffster
 *
 */
public final class ShapeBoundInf implements IBoundInference {
    private final ShapeSet shapes;

    @Override
    public IBoundInference negate() {
        return create(shapes.not());
    }

    private ShapeBoundInf(ShapeSet shapes) {
        this.shapes = shapes;
    }

    @Override
    public InfSummary getSummary() {
        return InfSummary.ALL.withShapes(shapes);
    }

    @Override
    public IBoundInference andWith(IBoundInference other) {
        if (other instanceof ShapeBoundInf) {
            return create(((ShapeBoundInf) other).shapes.and(shapes));
        }
        return null;
    }

    @Override
    public IBoundInference orWith(IBoundInference other) {
        if (other instanceof ShapeBoundInf) {
            return create(((ShapeBoundInf) other).shapes.or(shapes));
        }
        return null;
    }

    public static IBoundInference create(ShapeSet r) {
        if (r.isEmpty()) {
            return ConstBoundInference.F;
        }
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        return new ShapeBoundInf(r);
    }

    @Override
    public boolean test(Hand hand) {
        return shapes.contains(hand.getShape());
    }

    @Override
    public String toString() {
        return shapes.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(shapes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShapeBoundInf other = (ShapeBoundInf) obj;
        return Objects.equals(shapes, other.shapes);
    }
}
