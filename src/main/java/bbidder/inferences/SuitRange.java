package bbidder.inferences;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.SymbolTable;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.utils.ListUtil;

/**
 * Represents the inference of a range of lengths of a suit.
 * 
 * @author goffster
 *
 */
public final class SuitRange extends Inference {
    private final Symbol symbol;
    private final Range rng;

    public SuitRange(Symbol symbol, Integer min, Integer max) {
        super();
        this.symbol = symbol;
        this.rng = Range.between(min, max, 13);
    }

    public SuitRange(Symbol symbol, Range r) {
        super();
        this.symbol = symbol;
        this.rng = r;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain;
        try {
            strain = symbol.getResolved();
        } catch (Exception e) {
            throw e;
        }
        return createBound(strain, rng);
    }

    @Override
    public List<Context> resolveSymbols(SymbolTable symbols) {
        return ListUtil.map(symbol.resolveSymbols(symbols).collect(Collectors.toList()), e -> new SuitRange(e.getSymbol(), rng).new Context(e.symbols));
    }

    private static IBoundInference createBound(int s, Range r) {
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(s, r)));
    }

    public static Inference valueOf(String str) {
        if (str == null) {
            return null;
        }
        RangeOf rng = RangeOf.valueOf(str);
        if (rng == null) {
            return null;
        }
        Symbol sym = SymbolParser.parseSymbol(rng.of);
        if (sym != null) {
            return new SuitRange(sym, Range.between(rng.min, rng.max, 13));
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return rng + " " + symbol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rng, symbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SuitRange other = (SuitRange) obj;
        return Objects.equals(rng, other.rng) && Objects.equals(symbol, other.symbol);
    }
}
