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
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.symbols.ConstSymbol;

public class RebiddableSecondSuit implements Inference {
    public final Symbol longer;
    public final Symbol shorter;

    public static Pattern PATT_FIT = Pattern.compile("\\s*rebiddable_2nd\\s+(.*)\\s+(.*)", Pattern.CASE_INSENSITIVE);

    public RebiddableSecondSuit(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strainLonger = longer.getResolved();
        int strainShorter = shorter.getResolved();
        return createrBound(strainLonger, strainShorter, players.me.infSummary, players.partner.infSummary);
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext context) {
        List<BiddingContext> l = new ArrayList<>();
        for (var e : context.resolveSymbols(longer).entrySet()) {
            for (var e2 : e.getValue().resolveSymbols(shorter).entrySet()) {
                l.add(e2.getValue().withInferenceAdded(new RebiddableSecondSuit(new ConstSymbol(e.getKey()), new ConstSymbol(e2.getKey()))));
            }
        }
        return l;
    }

    private static IBoundInference createrBound(int strainLonger, int strainShorter, InfSummary meSummary, InfSummary partnerSummary) {
        int n = meSummary.minLenInSuit(strainShorter);
        Range r;
        if (n <= 0) {
            r = Range.atLeast(5, 13);
        } else {
            r = Range.atLeast(Math.max(n + 1, 5), 13);
        }
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(strainShorter, r) && shape.isLongerOrEqual(strainLonger, 1 << strainShorter)));
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = PATT_FIT.matcher(str);
        if (m.matches()) {
            Symbol longer = SymbolParser.parseSymbol(m.group(1).trim());
            Symbol shorter = SymbolParser.parseSymbol(m.group(2).trim());
            if (longer == null || shorter == null) {
                return null;
            }
            return new RebiddableSecondSuit(longer, shorter);
        }
        return null;
    }

    @Override
    public String toString() {
        return "rebiddable_2nd " + longer + " " + shorter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longer, shorter);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RebiddableSecondSuit other = (RebiddableSecondSuit) obj;
        return Objects.equals(longer, other.longer) && Objects.equals(shorter, other.shorter);
    }
    
}
