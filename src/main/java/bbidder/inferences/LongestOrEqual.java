package bbidder.inferences;

import java.util.Objects;

import bbidder.BitUtil;
import bbidder.Context;
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

    @Override
    public String toString() {
        return "longest_or_equal " + suit + (among == null ? "" : " among " + among);
    }

    public LongestOrEqual(String suit, String among) {
        super();
        this.suit = suit;
        this.among = among;
    }

    @Override
    public IBoundInference bind(Context context) {
        int isuit = context.lookupSuit(suit);
        int iamong = among == null ? 0xf : context.lookupSuitSet(among);
        return new BoundInf(isuit, iamong);
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

    static class BoundInf implements IBoundInference {
        final int isuit;
        final int iamong;

        public BoundInf(int isuit, int iamong) {
            super();
            this.isuit = isuit;
            this.iamong = iamong;
        }

        @Override
        public boolean matches(Hand hand) {
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
            StringBuilder sb = new StringBuilder();
            for (int s : BitUtil.iterate(iamong)) {
                sb.append("CDHS".charAt(s));
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
            return "longest_or_equal " + "CDHS".charAt(isuit) + sm;
        }
    }

}
