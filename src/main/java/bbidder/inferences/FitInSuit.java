package bbidder.inferences;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.ShapeSet;
import bbidder.SuitLengthRange;
import bbidder.SuitTable;
import bbidder.Symbol;
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

    public static Pattern PATT_FIT = Pattern.compile("\\s*fit\\s*(.*)", Pattern.CASE_INSENSITIVE);

    public FitInSuit(Symbol suit) {
        super();
        this.symbol = suit;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = symbol.getResolvedStrain();
        return createrBound(strain, players);
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new FitInSuit(e.getSymbol()).new Context(e.suitTable));
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

    private IBoundInference createrBound(int s, Players players) {
        OptionalInt minLenInSuit = players.partner.infSummary.minLenInSuit(s);
        if (!minLenInSuit.isPresent()) {
            return ConstBoundInference.F;
        }
        int partnerLen = minLenInSuit.getAsInt();
        int myMinLen = 8 - partnerLen;
        if (myMinLen <= 0) {
            return ConstBoundInference.T;
        } else {
            SuitLengthRange r = SuitLengthRange.atLeast(myMinLen);
            return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(s, r)));
        }
    }
}
