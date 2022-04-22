package bbidder.inferences;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.ListUtil;
import bbidder.Players;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.SymbolTable;
import bbidder.inferences.bound.ShapeBoundInf;

public final class Rebiddable implements Inference {
    public final Symbol symbol;

    public static Pattern PATT_FIT = Pattern.compile("\\s*rebiddable\\s*(.*)", Pattern.CASE_INSENSITIVE);

    public Rebiddable(Symbol suit) {
        super();
        this.symbol = suit;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = symbol.getResolved();
        return createrBound(strain, players.me.infSummary, players.partner.infSummary);
    }

    @Override
    public List<InferenceContext> resolveSymbols(SymbolTable symbols) {
        return ListUtil.map(symbol.resolveSymbols(symbols), e -> new InferenceContext(new Rebiddable(e.symbol), e.symbols));
    }

    private IBoundInference createrBound(int s, InfSummary meSummary, InfSummary partnerSummary) {
        int n = meSummary.minLenInSuit(s);
        Range r;
        if (n <= 0) {
            r = Range.atLeast(6, 13);
        } else {
            r = Range.atLeast(Math.max(n + 1, 6), 13);
        }
        IBoundInference create = ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(s, r)));
        return create;
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
            return new Rebiddable(sym);
        }
        return null;
    }

    @Override
    public String toString() {
        return "rebiddable " + symbol;
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
        Rebiddable other = (Rebiddable) obj;
        return Objects.equals(symbol, other.symbol);
    }
}
