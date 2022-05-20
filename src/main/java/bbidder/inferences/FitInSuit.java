package bbidder.inferences;

import java.util.Objects;
import java.util.OptionalInt;
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
    public static final String NAME = "fit";
    private final Symbol symbol;

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
        return NAME + " " + symbol;
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
        OptionalInt minLenInSuitPartner = players.partner.infSummary.minLenInSuit(s);
        if (!minLenInSuitPartner.isPresent()) {
            return ConstBoundInference.F;
        }
        int partnerLen = minLenInSuitPartner.getAsInt();
        OptionalInt minLenInSuitMe = players.me.infSummary.minLenInSuit(s);
        if (!minLenInSuitPartner.isPresent()) {
            return ConstBoundInference.F;
        }
        int myLen = minLenInSuitMe.getAsInt();
        if (myLen + partnerLen >= 8) {
            return ConstBoundInference.T;
        }
        OptionalInt bidSuits = players.partner.infSummary.getBidSuits();
        if (bidSuits.isEmpty()) {
            return ConstBoundInference.F;
        }
        if ((bidSuits.getAsInt() & (1 << s)) == 0) {
            return ConstBoundInference.F;
        }

        int myMinLen = 8 - partnerLen;
        if (myMinLen <= 0) {
            return ConstBoundInference.T;
        } else {
            SuitLengthRange r = SuitLengthRange.atLeast(myMinLen);
            return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(s, r)));
        }
    }
}
