package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.BiddingContext;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.Strain;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;

public class Rebiddable implements Inference {
    public final String suit;

    public static Pattern PATT_FIT = Pattern.compile("\\s*rebiddable\\s*(.*)", Pattern.CASE_INSENSITIVE);

    public Rebiddable(String suit) {
        super();
        this.suit = suit;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = Strain.getStrain(suit);
        return createrBound(strain, players.me.infSummary, players.partner.infSummary);
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext context) {
        List<BiddingContext> l = new ArrayList<>();
        for (var e : context.resolveSymbols(suit).entrySet()) {
            l.add(e.getValue().withInferenceAdded(new Rebiddable(Strain.getName(e.getKey()))));
        }
        return l;
    }

    private IBoundInference createrBound(int s, InfSummary meSummary, InfSummary partnerSummary) {
        if (meSummary.getSuit(s).lowest() + partnerSummary.getSuit(s).lowest() >= 8) {
            return ConstBoundInference.F;
        }
        int n = meSummary.getSuit(s).lowest();
        Range r;
        if (n <= 0) {
            r = Range.atLeast(6, 13);
        } else {
            r = Range.atLeast(Math.min(n + 1, 6), 13);
        }
        return ShapeBoundInf.create(new ShapeSet(shape -> shape.isSuitInRange(s, r)));
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = PATT_FIT.matcher(str);
        if (m.matches()) {
            return new Rebiddable(m.group(1).trim());
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
