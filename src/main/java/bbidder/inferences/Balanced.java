package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Shape;
import bbidder.ShapeSet;

/**
 * Represents the inference of a "balanced" hand
 * 
 * @author goffster
 *
 */
public class Balanced implements Inference {
    private static boolean isBalanced(Shape hand) {
        int ndoub = 0;
        for (int s = 0; s < 4; s++) {
            int len = hand.numInSuit(s);
            if (len < 2) {
                return false;
            }
            if (len == 2) {
                ndoub++;
            }
        }
        return ndoub <= 1;
    }

    public Balanced() {
        super();
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        List<Shape> shapes = new ArrayList<>();
        for(Shape shape: Shape.values()) {
            if (isBalanced(shape)) {
                shapes.add(shape);
            }
        }
        return ShapeBoundInference.create(new ShapeSet(shapes));
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
