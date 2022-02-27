package bbidder.inferences;

import java.util.Objects;

import bbidder.BitUtil;
import bbidder.Context;
import bbidder.Hand;
import bbidder.Inference;

public class LongestOrEqualLowerRanking implements Inference {
    public final String suit;
    public final String among;
    
    public static LongestOrEqualLowerRanking valueOf(String str) {
        str = str.trim();
        if (!str.toLowerCase().startsWith("longest_or_equal_lower_ranking")) {
            return null;
        }
        str = str.substring(31).trim();
        int pos = str.indexOf("among");
        if (pos >= 0) {
            return new LongestOrEqualLowerRanking(str.substring(0, pos).trim(), str.substring(pos + 5).trim());
        }
        return new LongestOrEqualLowerRanking(str, null);
    }

    @Override
    public String toString() {
        return "longest_or_equal_lower_ranking " + suit + (among == null ? "" : " among " + among);
    }

    public LongestOrEqualLowerRanking(String suit, String among) {
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
            if (len2 == len && s < isuit) {
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
        LongestOrEqualLowerRanking other = (LongestOrEqualLowerRanking) obj;
        return Objects.equals(among, other.among) && Objects.equals(suit, other.suit);
    }
}
