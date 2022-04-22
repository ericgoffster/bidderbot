package bbidder.inferences;

import java.util.List;

import bbidder.BiddingContext;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.MappedInf;
import bbidder.MappedInference;
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
    public List<MappedInference> bind(InferenceContext context) {
            return List.of(new MappedInference(ShapeBoundInf.create(new ShapeSet(Shape::isBalanced)), context));
    }
    
    @Override
    public List<MappedInf> resolveSuits(BiddingContext context) {
        return List.of(new MappedInf(this, context));
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
