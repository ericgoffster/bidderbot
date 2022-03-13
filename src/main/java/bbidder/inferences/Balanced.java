package bbidder.inferences;

import java.util.List;
import java.util.Objects;

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
public class Balanced implements Inference {
    final BalanceType type;

    public Balanced(BalanceType type) {
        this.type = type;
    }

    @Override
    public List<MappedInference> bind(InferenceContext context) {
        switch (type) {
        case regular:
            return List.of(new MappedInference(ShapeBoundInf.create(new ShapeSet(Shape::isBalanced)), context));
        case un:
            return List.of(new MappedInference(ShapeBoundInf.create(new ShapeSet(Shape::isBalanced).not()), context));
        case very:
            return List.of(new MappedInference(ShapeBoundInf.create(new ShapeSet(Shape::isSuperBalanced)), context));
        default:
            throw new IllegalStateException();
        }
    }

    public static Balanced valueOf(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("balanced")) {
            return new Balanced(BalanceType.regular);
        }
        if (str.toLowerCase().equals("unbalanced")) {
            return new Balanced(BalanceType.un);
        }
        if (str.toLowerCase().equals("superbalanced")) {
            return new Balanced(BalanceType.very);
        }
        return null;
    }

    @Override
    public String toString() {
        switch (type) {
        case regular:
            return "balanced";
        case un:
            return "unbalanced";
        case very:
            return "superbalanced";
        default:
            throw new IllegalStateException();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Balanced other = (Balanced) obj;
        return type == other.type;
    }
}
