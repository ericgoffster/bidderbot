package bbidder.inferences;

import java.util.OptionalInt;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.PointRange;
import bbidder.SuitTable;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.TotalPtsBoundInf;
import bbidder.utils.MyStream;

public final class MaxTpts extends Inference {
    public static final String TPTS = "tpts";
    public static MaxTpts MAX_TPTS_RANGE = new MaxTpts();

    private MaxTpts() {
        super();
    }

    @Override
    public IBoundInference bind(Players players) {
        OptionalInt max = players.me.infSummary.tpts.highest();
        if (!max.isPresent()) {
            return ConstBoundInference.F;
        }
        PointRange rng = PointRange.exactly(max.getAsInt());
        return TotalPtsBoundInf.create(players.partner.infSummary, rng);
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return "max " + TPTS;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
