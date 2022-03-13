package bbidder.inferences;

import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Range;
import bbidder.inferences.bound.TotalPtsBoundInf;

/**
 * Represents the inference of the total points in a suit.
 * 
 * @author goffster
 *
 */
public class TotalPointsRange implements Inference {
    public final Range rng;

    public TotalPointsRange(Integer min, Integer max) {
        super();
        this.rng = Range.between(min, max, 40);
    }
    public TotalPointsRange(Range rng) {
        super();
        this.rng = rng;
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        return TotalPtsBoundInf.createBounded(context.likelyHands.partner.getSummary(), rng);
    }
    
    public static Inference valueOf(String str) {
        return SuitRange.valueOf(str);
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
