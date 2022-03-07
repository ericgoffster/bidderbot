package bbidder.inferences;

import java.util.Objects;
import static bbidder.Constants.*;

import bbidder.BitUtil;
import bbidder.InferenceContext;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;

public class LongestOrEqual implements Inference {
    public final String suit;
    public final String among;

    public static LongestOrEqual valueOf(String str) {
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

    private static boolean isLongerOrEqual(int isuit, int iamong, Hand hand) {
        int len = hand.numInSuit(isuit);
        for (int s : BitUtil.iterate(iamong)) {
            int len2 = hand.numInSuit(s);
            if (len2 > len) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "longest_or_equal " + suit + (among == null ? "" : " among " + among);
    }

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
        return new LongestOrEqualBoundInf(isuit, iamong);
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

    private static String getAmong(int iamong2) {
        StringBuilder sb = new StringBuilder();
        for (int s : BitUtil.iterate(iamong2)) {
            sb.append(STR_ALL_SUITS.charAt(s));
        }
        String sm = " among " + sb;
        switch (sm) {
        case " among CDHS":
            sm = "";
            break;
        case " among HS":
            sm = " among majors";
            break;
        case " among CD":
            sm = " among minors";
            break;
        }
        return sm;
    }

    static class LongestOrEqualBoundInf implements IBoundInference {
        final int isuit;
        final int iamong;

        public LongestOrEqualBoundInf(int isuit, int iamong) {
            super();
            this.isuit = isuit;
            this.iamong = iamong;
        }

        @Override
        public boolean matches(Hand hand) {
            return isLongerOrEqual(isuit, iamong, hand);
        }

        @Override
        public boolean negatable() {
            return true;
        }

        @Override
        public IBoundInference negate() {
            return new ShorterBoundInf(isuit, iamong);
        }

        @Override
        public String toString() {
            return "longest_or_equal " + STR_ALL_SUITS.charAt(isuit) + getAmong(iamong);
        }
    }

    static class ShorterBoundInf implements IBoundInference {
        final int isuit;
        final int iamong;

        public ShorterBoundInf(int isuit, int iamong) {
            super();
            this.isuit = isuit;
            this.iamong = iamong;
        }

        @Override
        public boolean matches(Hand hand) {
            return !isLongerOrEqual(isuit, iamong, hand);
        }

        @Override
        public boolean negatable() {
            return false;
        }

        @Override
        public IBoundInference negate() {
            return new LongestOrEqualBoundInf(isuit, iamong);
        }

        @Override
        public String toString() {
            return "shorter " + STR_ALL_SUITS.charAt(isuit) + getAmong(iamong);
        }
    }
}
