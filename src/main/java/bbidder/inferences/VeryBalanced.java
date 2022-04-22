package bbidder.inferences;

import java.util.List;

import bbidder.BiddingContext;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a "balanced" hand
 * 
 * @author goffster
 *
 */
public class VeryBalanced implements Inference {
    public VeryBalanced() {
    }

    @Override
    public IBoundInference bind(Players players) {
        return ShapeBoundInf.create(new ShapeSet(Shape::isSuperBalanced));
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext context) {
        return List.of(context.withInferenceAdded(this));
    }

    public static VeryBalanced valueOf(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("superbalanced")) {
            return new VeryBalanced();
        }
        return null;
    }

    @Override
    public String toString() {
        return "superbalanced";
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
