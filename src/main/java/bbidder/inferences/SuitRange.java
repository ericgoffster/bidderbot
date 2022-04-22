package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bbidder.BiddingContext;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.Symbol;
import bbidder.SymbolParser;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.symbols.BoundSymbol;

/**
 * Represents the inference of a range of lengths of a suit.
 * 
 * @author goffster
 *
 */
public class SuitRange implements Inference {
    public final Symbol suit;
    public final Range rng;

    public SuitRange(Symbol suit, Integer min, Integer max) {
        super();
        this.suit = suit;
        this.rng = Range.between(min, max, 13);
    }

    public SuitRange(Symbol suit, Range r) {
        super();
        this.suit = suit;
        this.rng = r;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain;
        try {
            strain = suit.getResolved();
        } catch (Exception e) {
            throw e;
        }
        return createBound(strain, rng);
    }

    @Override
    public List<BiddingContext> resolveSymbols(BiddingContext context) {
        List<BiddingContext> l = new ArrayList<>();
        for (var e : context.resolveSymbols(suit).entrySet()) {
            l.add(e.getValue().withInferenceAdded(new SuitRange(new BoundSymbol(e.getKey(), suit), rng)));
        }
        return l;
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
        return rng + " " + suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rng, suit);
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
        return Objects.equals(rng, other.rng) && Objects.equals(suit, other.suit);
    }
}
