package bbidder.inferences;

import java.util.Objects;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.SplitUtil;

/**
 * Represents the inference for a high card point range.
 * 
 * @author goffster
 *
 */
public class HCPRange implements Inference {
    public final Integer min;
    public final Integer max;

    public HCPRange(Integer min, Integer max) {
        super();
        this.min = min;
        this.max = max;
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        Range r = Range.between(min, max, 40);
        return createBound(r);
    }

    private static IBoundInference createBound(Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.bits == 0) {
            return ConstBoundInference.F;
        }
        return new BoundInf(r);
    }

    public static HCPRange valueOf(String str) {
        if (str == null) {
            return null;
        }
        String[] hcpParts = SplitUtil.split(str, "\\s+", 2);
        if (hcpParts.length != 2 || !hcpParts[1].equalsIgnoreCase("hcp")) {
            return null;
        }
        str = hcpParts[0].trim();
        if (str.endsWith("+")) {
            return new HCPRange(Integer.parseInt(str.substring(0, str.length() - 1).trim()), null);
        }
        if (str.endsWith("-")) {
            return new HCPRange(null, Integer.parseInt(str.substring(0, str.length() - 1).trim()));
        }
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 1) {
            return new HCPRange(Integer.parseInt(parts[0]), Integer.parseInt(parts[0]));
        }
        if (parts.length != 2) {
            return null;
        }
        return new HCPRange(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    @Override
    public String toString() {
        if (max == null) {
            return min + "+ hcp";
        }
        if (min == null) {
            return max + "- hcp";
        }
        if (min.equals(max)) {
            return min + " hcp";
        }
        return min + "-" + max + " hcp";
    }

    @Override
    public int hashCode() {
        return Objects.hash(max, min);
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
        return Objects.equals(max, other.max) && Objects.equals(min, other.min);
    }

    static class BoundInf implements IBoundInference {
        final Range r;

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
