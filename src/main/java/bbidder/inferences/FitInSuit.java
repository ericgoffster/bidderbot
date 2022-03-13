package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.MappedInference;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a range of lengths of a suit.
 * 
 * @author goffster
 *
 */
public class FitInSuit implements Inference {
    public final String suit;

    public static Pattern PATT_FIT = Pattern.compile("\\s*fit\\s*(.*)", Pattern.CASE_INSENSITIVE);

    public FitInSuit(String suit) {
        super();
        this.suit = suit;
    }

    @Override
    public List<MappedInference> bind(InferenceContext context) {
        List<MappedInference> l = new ArrayList<>();
        for (var e : context.lookupSuits(suit).entrySet()) {
            l.add(new MappedInference(createrBound(e.getKey(), context.players.partner.infSummary), e.getValue()));
        }
        return l;
    }

    private IBoundInference createrBound(int s, InfSummary partnerSummary) {
        int n = 8 - partnerSummary.getSuit(s).lowest();
        if (n <= 0) {
            return ConstBoundInference.T;
        }
        Range r = Range.atLeast(n, 13);
        return ShapeBoundInf.create(new ShapeSet(shape -> shape.isSuitInRange(s, r)));
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = PATT_FIT.matcher(str);
        if (m.matches()) {
            return new FitInSuit(m.group(1).trim());
        }
        return null;
    }

    @Override
    public String toString() {
        return "fit " + suit;
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
        FitInSuit other = (FitInSuit) obj;
        return Objects.equals(suit, other.suit);
    }
}