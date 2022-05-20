package bbidder.inferences;

import java.util.OptionalInt;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.PointRange;
import bbidder.SuitTable;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.HcpBoundInf;
import bbidder.utils.MyStream;

public final class MaxHCPRange extends Inference {
    public static final String HCP = "hcp";
    public static MaxHCPRange MAX_HCP_RANGE = new MaxHCPRange();

    private MaxHCPRange() {
        super();
    }

    @Override
    public IBoundInference bind(Players players) {
        OptionalInt max = players.me.infSummary.hcp.highest();
        if (!max.isPresent()) {
            return ConstBoundInference.F;
        }
        PointRange rng = PointRange.exactly(max.getAsInt());
        return HcpBoundInf.create(rng);
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return "max " + HCP;
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
