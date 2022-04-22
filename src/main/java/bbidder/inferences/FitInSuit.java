package bbidder.inferences;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Inference;
import bbidder.ListUtil;
import bbidder.Players;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.SymbolTable;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;

/**
 * Represents the inference of a range of lengths of a suit.
 * 
 * @author goffster
 *
 */
public final class FitInSuit extends Inference {
    private final Symbol symbol;

    private static Pattern PATT_FIT = Pattern.compile("\\s*fit\\s*(.*)", Pattern.CASE_INSENSITIVE);

    public FitInSuit(Symbol suit) {
        super();
        this.symbol = suit;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = symbol.getResolved();
        return createrBound(strain, players.partner.infSummary);
    }

    @Override
    public List<Context> resolveSymbols(SymbolTable symbols) {
        return ListUtil.map(symbol.resolveSymbols(symbols), e -> new FitInSuit(e.getSymbol()).new Context(e.symbols));
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
        return "fit " + symbol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
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
        return Objects.equals(symbol, other.symbol);
    }

    private IBoundInference createrBound(int s, InfSummary partnerSummary) {
        int n = 8 - partnerSummary.minLenInSuit(s);
        if (n <= 0) {
            return ConstBoundInference.T;
        }
        Range r = Range.atLeast(n, 13);
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(s, r)));
    }
}
