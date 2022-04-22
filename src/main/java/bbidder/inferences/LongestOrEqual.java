package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.BiddingContext;
import bbidder.Constants;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.ShapeSet;
import bbidder.Strain;
import bbidder.SuitSets;
import bbidder.SuitSets.SuitSet;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a suit than is longest or equal among other suits.
 * 
 * @author goffster
 *
 */
public class LongestOrEqual implements Inference {
    public final String suit;
    public final SuitSet among;

    public LongestOrEqual(String suit, SuitSet among) {
        this.suit = suit;
        this.among = among;
    }

    @Override
    public IBoundInference bind(Players players) {
        int iamong = among == null ? 0xf : among.evaluate(players);
        int strain = Strain.getStrain(suit);
        return ShapeBoundInf.create(new ShapeSet(shape -> shape.isLongerOrEqual(strain, iamong)));
    }
    
    @Override
    public List<BiddingContext> resolveSuits(BiddingContext context) {
        List<BiddingContext> l = new ArrayList<>();
        for (var e : context.resolveSuits(suit).entrySet()) {
            l.add(e.getValue().withInferenceAdded(new LongestOrEqual(String.valueOf(Constants.STR_ALL_SUITS.charAt(e.getKey())), among.replaceVars(context))));
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
            return new LongestOrEqual(str.substring(0, pos).trim(), SuitSets.lookupSuitSet(str.substring(pos + 5).trim()));
        }
        if (!BiddingContext.isValidSuit(str)) {
            return null;
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
}
