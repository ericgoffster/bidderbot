package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.NOfTop;
import bbidder.Players;
import bbidder.Range;
import bbidder.Symbol;
import bbidder.SymbolContext;
import bbidder.SymbolParser;
import bbidder.SymbolTable;
import bbidder.inferences.bound.SpecificCardsBoundInf;

/**
 * Represents the inference of a specific cards in a suit.
 */
public class SpecificCards implements Inference {
    public final Symbol suit;
    public final Range rng;
    public final int top;

    public static Pattern PATT = Pattern.compile("of\\s+top\\s+(\\d+)\\s+in\\s+(.*)", Pattern.CASE_INSENSITIVE);

    public SpecificCards(Symbol suit, Range rng, int top) {
        super();
        this.suit = suit;
        this.rng = rng;
        this.top = top;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = suit.getResolved();
        return createBound(new NOfTop(rng, top, strain));
    }

    @Override
    public List<InferenceContext> resolveSymbols(SymbolTable suits) {
        List<InferenceContext> l = new ArrayList<>();
        for (var e : SymbolContext.resolveSymbols(suits, suit).entrySet()) {
            l.add(new InferenceContext(new SpecificCards(e.getKey(), rng, top), e.getValue()));
        }
        return l;
    }

    private static IBoundInference createBound(NOfTop spec) {
        return SpecificCardsBoundInf.create(spec);
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        RangeOf rng = RangeOf.valueOf(str);
        if (rng != null) {
            Matcher m = PATT.matcher(rng.of);
            if (m.matches()) {
                int top = Integer.parseInt(m.group(1));
                String suit = m.group(2);
                Symbol sym = SymbolParser.parseSymbol(suit);
                if (sym == null) {
                    return null;
                }
                return new SpecificCards(sym, Range.between(rng.min, rng.max, top), top);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return rng + " of top " + top + " in " + suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rng, suit, top);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpecificCards other = (SpecificCards) obj;
        return Objects.equals(rng, other.rng) && Objects.equals(suit, other.suit) && top == other.top;
    }
}
