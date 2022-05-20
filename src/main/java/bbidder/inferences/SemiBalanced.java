package bbidder.inferences;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.SuitTable;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.utils.MyStream;

/**
 * Represents the inference of a "balanced" hand
 * 
 * @author goffster
 *
 */
public final class SemiBalanced extends Inference {
    public static final String NAME = "semibalanced";
    public static final SemiBalanced SEMI_BALANCED = new SemiBalanced();

    private SemiBalanced() {
    }

    @Override
    public IBoundInference bind(Players players) {
        return ShapeBoundInf.create(ShapeSet.create(Shape::isSemiBalanced));
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return NAME;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
}
