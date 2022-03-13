package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Range;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.SplitUtil;

/**
 * Represents the inference of a range of lengths of a suit.
 * 
 * @author goffster
 *
 */
public class SuitRange implements Inference {
    public final String suit;
    public final Range rng;

    public SuitRange(String suit, Integer min, Integer max) {
        super();
        this.suit = suit;
        this.rng = Range.between(min, max, 13);
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        return createBound(context.lookupSuit(suit), rng);
    }

    private static IBoundInference createBound(int s, Range r) {
        List<Shape> shapes = new ArrayList<>();
        for(Shape shape: Shape.values()) {
            if (r.contains(shape.numInSuit(s))) {
                shapes.add(shape);
            }
        }
        return ShapeBoundInference.create(new ShapeSet(shapes));
    }

    public static SuitRange valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] bidParts = SplitUtil.split(str, "\\s+", 2);
        if (bidParts.length != 2) {
            return null;
        }
        String suit = bidParts[1].trim();
        if (suit.equalsIgnoreCase("hcp")) {
            return null;
        }
        str = bidParts[0].trim();
        if (str.endsWith("+")) {
            return new SuitRange(suit, Integer.parseInt(str.substring(0, str.length() - 1).trim()), null);
        }
        if (str.endsWith("-")) {
            return new SuitRange(suit, null, Integer.parseInt(str.substring(0, str.length() - 1).trim()));
        }
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 1) {
            return new SuitRange(suit, Integer.parseInt(parts[0]), Integer.parseInt(parts[0]));
        }
        if (parts.length != 2) {
            return null;
        }
        return new SuitRange(suit, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    @Override
    public String toString() {
        return rng + " " + suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rng, suit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SuitRange other = (SuitRange) obj;
        return Objects.equals(rng, other.rng) && Objects.equals(suit, other.suit);
    }
}
