package bbidder.inferences;

import java.util.Objects;
import static bbidder.Constants.*;

import bbidder.Context;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;

public class SuitRange implements Inference {
    public final String suit;
    public final String min;
    public final String max;

    public static SuitRange valueOf(String str) {
        str = str.trim();
        int pos = str.indexOf(" in ");
        if (pos < 0) {
            return null;
        }
        String suit = str.substring(pos + 4).trim();
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
            return min + "+ in " + suit;
        }
        if (min == null) {
            return max + "- in " + suit;
        }
        if (min.equals(max)) {
            return min + " in " + suit;
        }
        return min + "-" + max + " in " + suit;
    }

    public SuitRange(String suit, String min, String max) {
        super();
        this.suit = suit;
        this.min = min;
        this.max = max;
    }

    @Override
    public IBoundInference bind(Context context) {
        return new BoundInf(context.lookupSuit(suit), min == null ? null : context.resolveLength(min),
                max == null ? null : context.resolveLength(max));
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
        final Integer min;
        final Integer max;

        public BoundInf(int suit, Integer imin, Integer imax) {
            super();
            this.suit = suit;
            this.min = imin;
            this.max = imax;
        }
        
        @Override
        public boolean negatable() {
            return true;
        }
        
        @Override
        public IBoundInference negate() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean matches(Hand hand) {
            int len = hand.numInSuit(suit);
            if (min != null && len < min) {
                return false;
            }
            if (max != null && len > max) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            char s = STR_ALL_SUITS.charAt(suit);
            if (max == null) {
                return min + "+ in " + s;
            }
            if (min == null) {
                return max + "- in " + s;
            }
            if (min.equals(max)) {
                return min + " in " + s;
            }
            return min + "-" + max + " in " + s;
        }
    }
}
