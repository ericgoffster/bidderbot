package bbidder.inferences;

import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.SuitTable;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a "balanced" hand
 * 
 * @author goffster
 *
 */
public final class Balanced extends Inference {
    public static final Balanced BALANCED = new Balanced(); 
    private Balanced() {
    }

    @Override
    public IBoundInference bind(Players players) {
        return ShapeBoundInf.create(ShapeSet.create(Shape::isBalanced));
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return Stream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return "balanced";
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
