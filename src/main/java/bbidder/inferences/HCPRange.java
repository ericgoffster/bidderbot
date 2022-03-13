package bbidder.inferences;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Range;

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
        return createBound(rng);
    }

    public static IBoundInference createBound(Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.bits == 0) {
            return ConstBoundInference.F;
        }
        return new BoundInf(r);
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

    static class BoundInf implements IBoundInference {
        final Range r;
        
        @Override
        public int size() {
            return 1;
        }

        public BoundInf(Range r) {
            this.r = r;
        }

        @Override
        public boolean matches(Hand hand) {
            return r.contains(hand.numHCP());
        }

        @Override
        public IBoundInference negate() {
            return new BoundInf(r.not());
        }

        @Override
        public String toString() {
            return r + " hcp";
        }

        @Override
        public IBoundInference andReduce(IBoundInference i) {
            if (i instanceof BoundInf) {
                return createBound(r.and(((BoundInf) i).r));
            }
            return null;
        }

        @Override
        public IBoundInference orReduce(IBoundInference i) {
            if (i instanceof BoundInf) {
                return createBound(r.or(((BoundInf) i).r));
            }
            return null;
        }
    }
}
