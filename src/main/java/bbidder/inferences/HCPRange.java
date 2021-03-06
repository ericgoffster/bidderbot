package bbidder.inferences;

import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.PointRange;
import bbidder.SuitTable;
import bbidder.inferences.bound.HcpBoundInf;
import bbidder.utils.MyStream;

/**
 * Represents the inference for a high card point range.
 * 
 * @author goffster
 *
 */
public final class HCPRange extends Inference {
    public static final String HCP = "hcp";
    private final PointRange rng;

    public HCPRange(Integer min, Integer max) {
        super();
        this.rng = PointRange.between(min, max);
    }

    public HCPRange(PointRange r) {
        super();
        this.rng = r;
    }

    @Override
    public IBoundInference bind(Players players) {
        return HcpBoundInf.create(rng);
    }

    @Override
    public MyStream<Context> resolveSuits(SuitTable suitTable) {
        return MyStream.of(new Context(suitTable));
    }

    @Override
    public String toString() {
        return rng + " " + HCP;
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
        HCPRange other = (HCPRange) obj;
        return Objects.equals(rng, other.rng);
    }
}
