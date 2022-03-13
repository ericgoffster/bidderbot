package bbidder.inferences;

import static bbidder.Constants.STR_ALL_SUITS;

import java.util.Objects;

import bbidder.InferenceContext;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;

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
        int s = context.lookupSuit(suit);
        if (min == null) {
            if (max == null) {
                return ConstBoundInference.T;
            }
            return new BoundInfMax(s, context.resolveLength(max));
        }
        if (max == null) {
            return new BoundInfMin(s, context.resolveLength(min));
        }
        int imin = context.resolveLength(min);
        int imax = context.resolveLength(max);
        if (imin == imax) {
            return new BoundInfExact(s, imin);
        }
        return AndBoundInference.create(new BoundInfMin(s, imin), new BoundInfMax(s, imax));
    }

    public static SuitRange valueOf(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        int pos = str.indexOf(" ");
        if (pos < 0) {
            return null;
        }
        String suit = str.substring(pos + 1).trim();
        if (suit.equalsIgnoreCase("hcp")) {
            return null;
        }
        str = str.substring(0, pos).trim();
        if (str.endsWith("+")) {
            return new SuitRange(suit, str.substring(0, str.length() - 1).trim(), null);
        }
        if (str.endsWith("-")) {
            return new SuitRange(suit, null, str.substring(0, str.length() - 1).trim());
        }
        String[] parts = str.split("-");
        if (parts.length == 1) {
            return new SuitRange(suit, parts[0].trim(), parts[0].trim());
        }
        if (parts.length != 2) {
            return null;
        }
        return new SuitRange(suit, parts[0].trim(), parts[1].trim());
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

    
    static class BoundInfExact implements IBoundInference {
        final int suit;
        final int n;

        public BoundInfExact(int suit, int n) {
            this.suit = suit;
            this.n = n;
        }

        @Override
        public boolean matches(Hand hand) {
            return hand.numInSuit(suit) == n;
        }

        @Override
        public boolean negatable() {
            return true;
        }

        @Override
        public IBoundInference negate() {
            return new BoundInfExactNot(suit, n);
        }

        @Override
        public String toString() {
            return n + " " + STR_ALL_SUITS.charAt(suit);
        }
    }

    static class BoundInfExactNot implements IBoundInference {
        final int suit;
        final int n;

        public BoundInfExactNot(int suit, int n) {
            this.suit = suit;
            this.n = n;
        }

        @Override
        public boolean matches(Hand hand) {
            return hand.numInSuit(suit) != n;
        }

        @Override
        public boolean negatable() {
            return true;
        }

        @Override
        public IBoundInference negate() {
            return new BoundInfExact(suit, n);
        }

        @Override
        public String toString() {
            return "~" + n + " " + STR_ALL_SUITS.charAt(suit);
        }
    }

    static class BoundInfMin implements IBoundInference {
        final int suit;
        final int min;

        public BoundInfMin(int suit, int min) {
            this.suit = suit;
            this.min = min;
        }

        @Override
        public boolean matches(Hand hand) {
            return hand.numInSuit(suit) >= min;
        }

        @Override
        public boolean negatable() {
            return true;
        }

        @Override
        public IBoundInference negate() {
            return new BoundInfMax(suit, min - 1);
        }

        @Override
        public String toString() {
            return min + "+ " + STR_ALL_SUITS.charAt(suit);
        }
    }

    static class BoundInfMax implements IBoundInference {
        final int suit;
        final int max;

        public BoundInfMax(int suit, int max) {
            this.suit = suit;
            this.max = max;
        }

        @Override
        public boolean matches(Hand hand) {
            return hand.numInSuit(suit) <= max;
        }

        @Override
        public boolean negatable() {
            return true;
        }

        @Override
        public IBoundInference negate() {
            return new BoundInfMin(suit, max + 1);
        }

        @Override
        public String toString() {
            return max + "- " + STR_ALL_SUITS.charAt(suit);
        }
    }

}
