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
public class Balanced implements Inference {
    public Balanced() {
    }

    @Override
    public IBoundInference bind(Players players) {
        return ShapeBoundInf.create(new ShapeSet(Shape::isBalanced));
    }

    @Override
    public List<BiddingContext> resolveSuits(BiddingContext context) {
        return List.of(context.withInferenceAdded(this));
    }

    public static Balanced valueOf(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("balanced")) {
            return new Balanced();
        }
        return null;
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
