package bbidder.inferences;

import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.ShapeSet;
import bbidder.SuitLengthRange;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.utils.MyStream;

/**
 * Represents the inference of a range of lengths of a suit.
 * 
 * @author goffster
 *
 */
public final class SuitRange extends Inference {
    private final Symbol symbol;
    private final SuitLengthRange rng;

    public SuitRange(Symbol symbol, Integer min, Integer max) {
        super();
        this.symbol = symbol;
        this.rng = SuitLengthRange.between(min, max);
    }

    public SuitRange(Symbol symbol, SuitLengthRange r) {
        super();
        this.symbol = symbol;
        this.rng = r;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain;
        try {
            strain = symbol.getResolvedStrain();
        } catch (Exception e) {
            throw e;
        }
        return createBound(strain, rng);
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new SuitRange(e.getSymbol(), rng).new Context(e.suitTable));
    }

    private static IBoundInference createBound(int s, SuitLengthRange r) {
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(s, r)));
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
