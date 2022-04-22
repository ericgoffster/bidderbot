package bbidder.inferences;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.NOfTop;
import bbidder.Players;
import bbidder.Range;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.SymbolTable;
import bbidder.inferences.bound.SpecificCardsBoundInf;
import bbidder.utils.ListUtil;

/**
 * Represents the inference of a specific cards in a suit.
 */
public final class SpecificCards extends Inference {
    private final Symbol symbol;
    private final Range rng;
    private final int top;

    private static Pattern PATT = Pattern.compile("of\\s+top\\s+(\\d+)\\s+in\\s+(.*)", Pattern.CASE_INSENSITIVE);

    public SpecificCards(Symbol symbol, Range rng, int top) {
        super();
        this.symbol = symbol;
        this.rng = rng;
        this.top = top;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = symbol.getResolved();
        return createBound(new NOfTop(rng, top, strain));
    }

    @Override
    public List<Context> resolveSymbols(SymbolTable symbols) {
        return ListUtil.map(symbol.resolveSymbols(symbols), e -> new SpecificCards(e.getSymbol(), rng, top).new Context(e.symbols));
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
        return rng + " of top " + top + " in " + symbol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rng, symbol, top);
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
        return Objects.equals(rng, other.rng) && Objects.equals(symbol, other.symbol) && top == other.top;
    }
}
