package bbidder.inferences;

import static bbidder.Constants.STR_ALL_SUITS;

import java.util.Objects;

import bbidder.InferenceContext;
import bbidder.SplitUtil;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;

/**
 * Represents the inference of a range of lengths of a suit.
 * 
 * @author goffster
 *
 */
public class SuitRange implements Inference {
    public final String suit;
    public final String min;
    public final String max;

    public SuitRange(String suit, String min, String max) {
        super();
        this.suit = suit;
        this.min = min;
        this.max = max;
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        Range r = Range.between(min == null ? null : context.resolveLength(min), max == null ? null : context.resolveLength(max), 13);
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        return new BoundInf(context.lookupSuit(suit), r);
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
            return new SuitRange(suit, str.substring(0, str.length() - 1).trim(), null);
        }
        if (str.endsWith("-")) {
            return new SuitRange(suit, null, str.substring(0, str.length() - 1).trim());
        }
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 1) {
            return new SuitRange(suit, parts[0], parts[0]);
        }
        if (parts.length != 2) {
            return null;
        }
        return new SuitRange(suit, parts[0], parts[1]);
    }

    @Override
    public String toString() {
        if (max == null) {
            return min + "+ " + suit;
        }
        if (min == null) {
            return max + "- " + suit;
        }
        if (min.equals(max)) {
            return min + " " + suit;
        }
        return min + "-" + max + " " + suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(max, min, suit);
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
        return Objects.equals(max, other.max) && Objects.equals(min, other.min) && Objects.equals(suit, other.suit);
    }

    static class BoundInf implements IBoundInference {
        final int suit;
        final Range range;

        public BoundInf(int suit, Range range) {
            this.suit = suit;
            this.range = range;
        }

        @Override
        public boolean matches(Hand hand) {
            return (range.bits & (1L << hand.numInSuit(suit))) != 0;
        }

        @Override
        public BoundInf negate() {
            return new BoundInf(suit, range.not());
        }

        @Override
        public String toString() {
            return range + " " + STR_ALL_SUITS.charAt(suit);
        }
    }
}
