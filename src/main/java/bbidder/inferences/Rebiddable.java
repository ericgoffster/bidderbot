package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.BiddingContext;
import bbidder.ConstSymbol;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.Symbol;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;

public class Rebiddable implements Inference {
    public final Symbol suit;

    public static Pattern PATT_FIT = Pattern.compile("\\s*rebiddable\\s*(.*)", Pattern.CASE_INSENSITIVE);

    public Rebiddable(Symbol suit) {
        super();
        this.suit = suit;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = suit.getResolved();
        return createrBound(strain, players.me.infSummary, players.partner.infSummary);
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext context) {
        List<BiddingContext> l = new ArrayList<>();
        for (var e : context.resolveSymbols(suit).entrySet()) {
            l.add(e.getValue().withInferenceAdded(new Rebiddable(new ConstSymbol(e.getKey()))));
        }
        return l;
    }

    private IBoundInference createrBound(int s, InfSummary meSummary, InfSummary partnerSummary) {
        if (meSummary.minLenInSuit(s) + partnerSummary.minLenInSuit(s) >= 8) {
            return ConstBoundInference.F;
        }
        if (meSummary.avgLenInSuit(s) < 4) {
            return ConstBoundInference.F;
        }
        int n = meSummary.minLenInSuit(s);
        Range r;
        if (n <= 0) {
            r = Range.atLeast(6, 13);
        } else {
            r = Range.atLeast(Math.min(n + 1, 6), 13);
        }
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(s, r)));
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = PATT_FIT.matcher(str);
        if (m.matches()) {
            Symbol sym = BiddingContext.parseSymbol(m.group(1).trim());
            if (sym == null) {
                return null;
            }
            return new Rebiddable(sym);
        }
        return null;
    }

    @Override
    public String toString() {
        return "rebiddable " + suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Rebiddable other = (Rebiddable) obj;
        return Objects.equals(suit, other.suit);
    }
}
