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
    public final String min;
    public final String max;

    public HCPRange(String min, String max) {
        super();
        this.min = min;
        this.max = max;
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        Range r = Range.between(min == null ? null : context.resolvePoints(min), max == null ? null : context.resolvePoints(max), 40);
        if (r.unBounded()) {
            return ConstBoundInference.T;
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
            return new HCPRange(str.substring(0, str.length() - 1).trim(), null);
        }
        if (str.endsWith("-")) {
            return new HCPRange(null, str.substring(0, str.length() - 1).trim());
        }
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 1) {
            return new HCPRange(parts[0], parts[0]);
        }
        if (parts.length != 2) {
            return null;
        }
        return new HCPRange(parts[0], parts[1]);
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
                Range r2 = ((BoundInf) i).r;
                Range newR = new Range(r.bits & r2.bits, r.max);
                if (newR.bits == 0) {
                    return ConstBoundInference.F;
                }
                return new BoundInf(newR);
            }
            return null;
        }
    }
}
