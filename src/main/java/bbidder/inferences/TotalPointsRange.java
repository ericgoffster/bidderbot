package bbidder.inferences;

import java.util.Objects;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.SymbolTable;
import bbidder.inferences.bound.TotalPtsBoundInf;

/**
 * Represents the inference of the total points in a suit.
 * 
 * @author goffster
 *
 */
public final class TotalPointsRange extends Inference {
    private final Range rng;

    public TotalPointsRange(Integer min, Integer max) {
        super();
        this.rng = Range.between(min, max, 40);
    }

    public TotalPointsRange(Range rng) {
        super();
        this.rng = rng;
    }

    @Override
    public IBoundInference bind(Players players) {
        return TotalPtsBoundInf.create(players.partner.infSummary, rng);
    }

    @Override
    public Stream<Context> resolveSymbols(SymbolTable symbols) {
        return Stream.of(new Context(symbols));
    }

    public static Inference valueOf(String str) {
        RangeOf rng = RangeOf.valueOf(str);
        if (rng == null) {
            return null;
        }
        if (rng.of.equalsIgnoreCase("tpts")) {
            return new TotalPointsRange(Range.between(rng.min, rng.max, 40));
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return rng + " tpts";
    }

    @Override
    public int hashCode() {
        return Objects.hash(rng);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TotalPointsRange other = (TotalPointsRange) obj;
        return Objects.equals(rng, other.rng);
    }
}
