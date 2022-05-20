package bbidder.inferences;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.SuitTable;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;

public final class RebiddableSecondSuit extends Inference {
    private final Symbol longer;
    private final Symbol shorter;

    private static Pattern PATT_FIT = Pattern.compile("\\s*rebiddable_2nd\\s+(.*)\\s+(.*)", Pattern.CASE_INSENSITIVE);

    public RebiddableSecondSuit(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strainLonger = longer.getResolvedStrain();
        int strainShorter = shorter.getResolvedStrain();
        return createrBound(strainLonger, strainShorter, players.me.infSummary, players.partner.infSummary);
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return longer.resolveSuits(suitTable)
                .flatMap(e1 -> shorter.resolveSuits(e1.suitTable)
                        .map(e2 -> new RebiddableSecondSuit(e1.getSymbol(), e2.getSymbol()).new Context(e2.suitTable)));
    }

    private static IBoundInference createrBound(int strainLonger, int strainShorter, InfSummary meSummary, InfSummary partnerSummary) {
        Optional<Integer> n = meSummary.minLenInSuit(strainShorter);
        if (!n.isPresent()) {
            return ConstBoundInference.F;
        }
        Range r = Range.atLeast(Math.max(n.get() + 1, 5), 13);
        return ShapeBoundInf
                .create(ShapeSet.create(shape -> shape.isSuitInRange(strainShorter, r) && shape.isLongerOrEqual(strainLonger, 1 << strainShorter)));
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
