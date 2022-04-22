package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.InferenceContext;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a range of lengths of a suit.
 * 
 * @author goffster
 *
 */
public class FitInSuit implements Inference {
    public final Symbol suit;

    public static Pattern PATT_FIT = Pattern.compile("\\s*fit\\s*(.*)", Pattern.CASE_INSENSITIVE);

    public FitInSuit(Symbol suit) {
        super();
        this.suit = suit;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = suit.getResolved();
        return createrBound(strain, players.partner.infSummary);
    }

    @Override
    public List<InferenceContext> resolveSymbols(InferenceContext context) {
        List<InferenceContext> l = new ArrayList<>();
        for (var e : context.resolveSymbols(suit).entrySet()) {
            l.add(e.getValue().withInferenceAdded(new FitInSuit(e.getKey())));
        }
        return l;
    }

    private IBoundInference createrBound(int s, InfSummary partnerSummary) {
        int n = 8 - partnerSummary.minLenInSuit(s);
        if (n <= 0) {
            return ConstBoundInference.T;
        }
        Range r = Range.atLeast(n, 13);
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(s, r)));
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = PATT_FIT.matcher(str);
        if (m.matches()) {
            Symbol sym = SymbolParser.parseSymbol(m.group(1).trim());
            if (sym == null) {
                return null;
            }
            return new FitInSuit(sym);
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
