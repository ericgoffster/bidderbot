package bbidder.inferences;

import java.util.List;

import bbidder.Inference;
import bbidder.InferenceContext;
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
public class VeryBalanced implements Inference {
    public VeryBalanced() {
    }

    @Override
    public List<MappedInference> bind(InferenceContext context) {
        return List.of(new MappedInference(ShapeBoundInf.create(new ShapeSet(Shape::isSuperBalanced)), context));
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
