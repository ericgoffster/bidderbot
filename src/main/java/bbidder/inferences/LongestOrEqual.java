package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.BitUtil;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Shape;
import bbidder.ShapeSet;

/**
 * Represents the inference of a suit than is longest or equal among other suits.
 * 
 * @author goffster
 *
 */
public class LongestOrEqual implements Inference {
    public final String suit;
    public final String among;

    public LongestOrEqual(String suit, String among) {
        if (among != null && among.trim().equals("")) {
            throw new IllegalArgumentException("among");
        }
        this.suit = suit;
        this.among = among;
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        int isuit = context.lookupSuit(suit);
        int iamong = among == null ? 0xf : context.lookupSuitSet(among);
        List<Shape> shapes = new ArrayList<>();
        for(Shape shape: Shape.values()) {
            if (isLongerOrEqual(isuit, iamong, shape)) {
                shapes.add(shape);
            }
        }
        return ShapeBoundInference.create(new ShapeSet(shapes));
    }

    public static LongestOrEqual valueOf(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (!str.toLowerCase().startsWith("longest_or_equal")) {
            return null;
        }
        str = str.substring(16).trim();
        int pos = str.indexOf("among");
        if (pos >= 0) {
            return new LongestOrEqual(str.substring(0, pos).trim(), str.substring(pos + 5).trim());
        }
        return new LongestOrEqual(str, null);
    }

    @Override
    public String toString() {
        return "longest_or_equal " + suit + (among == null ? "" : " among " + among);
    }

    @Override
    public int hashCode() {
        return Objects.hash(among, suit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LongestOrEqual other = (LongestOrEqual) obj;
        return Objects.equals(among, other.among) && Objects.equals(suit, other.suit);
    }

    private static boolean isLongerOrEqual(int isuit, int iamong, Shape hand) {
        int len = hand.numInSuit(isuit);
        for (int s : BitUtil.iterate(iamong)) {
            int len2 = hand.numInSuit(s);
            if (len2 > len) {
                return false;
            }
        }
        return true;
    }
}
