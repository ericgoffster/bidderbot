package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.BiddingContext;
import bbidder.Constants;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.MappedInf;
import bbidder.Players;
import bbidder.ShapeSet;
import bbidder.Strain;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a suit than is longest or equal among other suits.
 * 
 * @author goffster
 *
 */
public class LongestOrEqual implements Inference {
    public final String suit;
    public final String among;
    public final BiddingContext ctx;

    public LongestOrEqual(String suit, String among, BiddingContext ctx) {
        if (among != null && among.trim().equals("")) {
            throw new IllegalArgumentException("among");
        }
        this.suit = suit;
        this.among = among;
        this.ctx = ctx;
    }

    @Override
    public IBoundInference bind(Players players) {
        InferenceContext context = new InferenceContext(players, ctx);
        int iamong = among == null ? 0xf : context.lookupSuitSet(among);
        int strain = Strain.getStrain(suit);
        return ShapeBoundInf.create(new ShapeSet(shape -> shape.isLongerOrEqual(strain, iamong)));
    }
    
    @Override
    public List<MappedInf> resolveSuits(BiddingContext context) {
        List<MappedInf> l = new ArrayList<>();
        for (var e : context.getMappedBiddingContexts(suit).entrySet()) {
            l.add(new MappedInf(new LongestOrEqual(String.valueOf(Constants.STR_ALL_SUITS.charAt(e.getKey())), among, context), e.getValue()));
        }
        return l;
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
            return new LongestOrEqual(str.substring(0, pos).trim(), str.substring(pos + 5).trim(), null);
        }
        if (!BiddingContext.isValidSuit(str)) {
            return null;
        }
        return new LongestOrEqual(str, null, null);
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
}
