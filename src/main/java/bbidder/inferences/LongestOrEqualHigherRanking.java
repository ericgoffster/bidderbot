package bbidder.inferences;

import java.util.Objects;

import bbidder.BitUtil;
import bbidder.Context;
import bbidder.Hand;
import bbidder.Inference;

public class LongestOrEqualHigherRanking implements Inference {
    public final String suit;
    public final String among;

    public static LongestOrEqualHigherRanking valueOf(String str) {
        str = str.trim();
        if (!str.toLowerCase().startsWith("longest_or_equal_higher_ranking")) {
            return null;
        }
        str = str.substring(31).trim();
        int pos = str.indexOf("among");
        if (pos >= 0) {
            return new LongestOrEqualHigherRanking(str.substring(0, pos).trim(), str.substring(pos + 5).trim());
        }
        return new LongestOrEqualHigherRanking(str, null);
    }

    @Override
    public String toString() {
        return "longest_or_equal_higher_ranking " + suit + (among == null ? "" : " among " + among);
    }

    public LongestOrEqualHigherRanking(String suit, String among) {
        super();
        this.suit = suit;
        this.among = among;
    }

    @Override
    public boolean matches(Context context, Hand hand) {
        int isuit = context.lookupSuit(suit);
        int iamong = among == null ? 0xf : context.lookupSuitSet(among);
        int len = hand.numInSuit(isuit);
        for (int s : BitUtil.iterate(iamong)) {
            int len2 = hand.numInSuit(s);
            if (len2 > len) {
                return false;
            }
            if (len2 == len && s > isuit) {
                return false;
            }
        }
        return true;
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
        LongestOrEqualHigherRanking other = (LongestOrEqualHigherRanking) obj;
        return Objects.equals(among, other.among) && Objects.equals(suit, other.suit);
    }
}
