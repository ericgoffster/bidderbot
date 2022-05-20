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
public final class UnBalanced extends Inference {

    public static final String NAME = "unbalanced";
    public static final UnBalanced UNBALANCED = new UnBalanced();

    private UnBalanced() {
    }

    @Override
    public IBoundInference bind(Players players) {
        return ShapeBoundInf.create(ShapeSet.create(Shape::isBalanced).not());
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
