package bbidder.inferences;

import java.util.Objects;

import bbidder.CardsRange;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.NOfTop;
import bbidder.Players;
import bbidder.SuitTable;
import bbidder.Symbol;
import bbidder.inferences.bound.SpecificCardsBoundInf;
import bbidder.utils.MyStream;

/**
 * Represents the inference of a specific cards in a suit.
 */
public final class SpecificCards extends Inference {
    private final Symbol symbol;
    private final CardsRange rng;
    private final int top;

    public SpecificCards(Symbol symbol, CardsRange rng, int top) {
        super();
        this.symbol = symbol;
        this.rng = rng;
        this.top = top;
    }

    @Override
    public IBoundInference bind(Players players) {
        int strain = symbol.getResolvedStrain();
        return createBound(new NOfTop(rng, top, strain));
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return symbol.resolveSuits(suitTable).map(e -> new SpecificCards(e.getSymbol(), rng, top).new Context(e.suitTable));
    }

    private static IBoundInference createBound(NOfTop spec) {
        return SpecificCardsBoundInf.create(spec);
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
