package bbidder.inferences;

import java.util.List;

import bbidder.BiddingContext;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.MappedInf;
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
public class UnBalanced implements Inference {

    public UnBalanced() {
    }

    @Override
    public IBoundInference bind(Players players) {
        return ShapeBoundInf.create(new ShapeSet(Shape::isBalanced).not());
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
