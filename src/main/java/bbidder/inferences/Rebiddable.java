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
import bbidder.Players;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.Symbol;
import bbidder.SymbolContext;
import bbidder.SymbolParser;
import bbidder.SymbolTable;
import bbidder.inferences.bound.ShapeBoundInf;

public final class Rebiddable implements Inference {
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
    public List<InferenceContext> resolveSymbols(SymbolTable suits) {
        List<InferenceContext> l = new ArrayList<>();
        for (var e : SymbolContext.resolveSymbols(suits, suit).entrySet()) {
            l.add(new InferenceContext(new Rebiddable(e.getKey()), e.getValue()));
        }
        return l;
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
