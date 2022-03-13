package bbidder.inferences;

import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Range;
import bbidder.inferences.bound.HcpBoundInf;

/**
 * Represents the inference for a high card point range.
 * 
 * @author goffster
 *
 */
public class HCPRange implements Inference {
    public final Range rng;

    public HCPRange(Integer min, Integer max) {
        super();
        this.rng = Range.between(min, max, 40);
    }
    public HCPRange(Range r) {
        super();
        this.rng = r;
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        return HcpBoundInf.create(rng);
    }

    public static Inference valueOf(String str) {
        return SuitRange.valueOf(str);
    }

    @Override
    public String toString() {
        return rng + " hcp";
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
