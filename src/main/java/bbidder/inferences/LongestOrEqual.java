package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.BiddingContext;
import bbidder.ConstSymbol;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.ShapeSet;
import bbidder.SuitSets;
import bbidder.SuitSets.SuitSet;
import bbidder.Symbol;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a suit than is longest or equal among other suits.
 * 
 * @author goffster
 *
 */
public class LongestOrEqual implements Inference {
    public final Symbol suit;
    public final SuitSet among;

    public LongestOrEqual(Symbol suit, SuitSet among) {
        this.suit = suit;
        this.among = among;
    }

    @Override
    public IBoundInference bind(Players players) {
        int iamong = among == null ? 0xf : among.evaluate(players);
        int strain = suit.getResolved();
        return ShapeBoundInf.create(new ShapeSet(shape -> shape.isLongerOrEqual(strain, iamong)));
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext context) {
        List<BiddingContext> l = new ArrayList<>();
        for (var e : context.resolveSymbols(suit).entrySet()) {
            l.add(e.getValue()
                    .withInferenceAdded(new LongestOrEqual(new ConstSymbol(e.getKey()), among.replaceVars(context))));
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
            Symbol sym = BiddingContext.parseSymbol(str.substring(0, pos).trim());
            if (sym == null) {
                return null;
            }
            return new LongestOrEqual(sym, SuitSets.lookupSuitSet(str.substring(pos + 5).trim()));
        }
        Symbol sym = BiddingContext.parseSymbol(str);
        if (sym == null) {
            return null;
        }
        return new LongestOrEqual(sym, null);
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
