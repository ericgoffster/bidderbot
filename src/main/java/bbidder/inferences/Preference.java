package bbidder.inferences;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;

public final class Preference extends Inference {
    private final Symbol longer;
    private final Symbol shorter;

    private static Pattern PATT_FIT = Pattern.compile("\\s*prefer\\s*(.*)\\s*to\\s*(.*)", Pattern.CASE_INSENSITIVE);

    public Preference(Symbol longer, Symbol shorter) {
        super();
        this.longer = longer;
        this.shorter = shorter;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain1 = longer.getResolvedStrain();
        int strain2 = shorter.getResolvedStrain();
        return createrBound(strain1, strain2, players);
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return longer.resolveSuits(suitTable)
                .flatMap(e1 -> shorter.resolveSuits(e1.suitTable).map(e2 -> new Preference(e1.getSymbol(), e2.getSymbol()).new Context(e2.suitTable)));
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = PATT_FIT.matcher(str);
        if (m.matches()) {
            Symbol sym1 = SymbolParser.parseSymbol(m.group(1).trim());
            if (sym1 == null) {
                return null;
            }
            Symbol sym2 = SymbolParser.parseSymbol(m.group(2).trim());
            if (sym2 == null) {
                return null;
            }
            return new Preference(sym1, sym2);
        }
        return null;
    }

    @Override
    public String toString() {
        return "prefer " + longer + " to " + shorter;
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
        Preference other = (Preference) obj;
        return Objects.equals(longer, other.longer) && Objects.equals(shorter, other.shorter);
    }
    
    public static boolean isBetterFit(Shape shape, int longer, int shorter, int partnerLenLonger, int partnerLenShorter) {
        int diff = partnerLenLonger + shape.numInSuit(longer) - (partnerLenShorter + shape.numInSuit(shorter));
        if (diff > 0) {
            return true;
        }
        if (diff < 0) {
            return false;
        }
        return shape.numInSuit(longer) >= shape.numInSuit(shorter);
    }

    private static IBoundInference createrBound(int longer, int shorter, Players players) {
        OptionalInt minLenInLonger = players.partner.infSummary.minLenInSuit(longer);
        if (!minLenInLonger.isPresent()) {
            return ConstBoundInference.F;
        }
        OptionalInt minLenInShorter = players.partner.infSummary.minLenInSuit(shorter);
        if (!minLenInShorter.isPresent()) {
            return ConstBoundInference.F;
        }
        int partnerLenLonger = minLenInLonger.getAsInt();
        int partnerLenShorter = minLenInShorter.getAsInt();
        return ShapeBoundInf.create(ShapeSet.create(shape -> isBetterFit(shape, longer, shorter, partnerLenLonger, partnerLenShorter)));
    }
}
