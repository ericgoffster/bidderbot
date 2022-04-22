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
public class UnBalanced implements Inference {

    public UnBalanced() {
    }

    @Override
    public List<MappedInference> bind(InferenceContext context) {
        return List.of(new MappedInference(ShapeBoundInf.create(new ShapeSet(Shape::isBalanced).not()), context));
    }
    
    @Override
    public List<MappedInf> resolveSuits(BiddingContext context) {
        return List.of(new MappedInf(this, context));
    }

    public static UnBalanced valueOf(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("unbalanced")) {
            return new UnBalanced();
        }
        return null;
    }

    @Override
    public String toString() {
        return "unbalanced";
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
