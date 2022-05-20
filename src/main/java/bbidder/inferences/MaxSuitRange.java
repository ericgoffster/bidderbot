package bbidder.inferences;

import java.util.Objects;
import java.util.OptionalInt;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.ShapeSet;
import bbidder.SuitLengthRange;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.ShapeBoundInf;
import bbidder.utils.MyStream;

public final class MaxSuitRange extends Inference {
    private final Symbol symbol;

    public MaxSuitRange(Symbol symbol) {
        super();
        this.symbol = symbol;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain;
        try {
            strain = symbol.getResolvedStrain();
        } catch (Exception e) {
            throw e;
        }
        OptionalInt max = players.me.infSummary.getSuit(strain).highest();
        if (!max.isPresent()) {
            return ConstBoundInference.F;
        }
        SuitLengthRange r = SuitLengthRange.exactly(max.getAsInt());
        return ShapeBoundInf.create(ShapeSet.create(shape -> shape.isSuitInRange(strain, r)));
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new MaxSuitRange(e.getSymbol()).new Context(e.suitTable));
    }

    @Override
    public String toString() {
        return symbol.toString();
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
        MaxSuitRange other = (MaxSuitRange) obj;
        return Objects.equals(symbol, other.symbol);
    }
}
