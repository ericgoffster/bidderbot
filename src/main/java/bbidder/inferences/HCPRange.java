package bbidder.inferences;

import java.util.Objects;

import bbidder.InferenceContext;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;

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
        if (min == null) {
            if (max == null) {
                return ConstBoundInference.T;
            }
            return new BoundInfMax(context.resolvePoints(max));
        }
        if (max == null) {
            return new BoundInfMin(context.resolvePoints(min));
        }
        return AndBoundInference.create(new BoundInfMin(context.resolvePoints(min)), new BoundInfMax(context.resolvePoints(max)));
    }

    public static HCPRange valueOf(String str) {
        str = str.trim();
        if (!str.toLowerCase().endsWith("hcp")) {
            return null;
        }
        str = str.substring(0, str.length() - 3).trim();
        if (str.endsWith("+")) {
            return new HCPRange(str.substring(0, str.length() - 1).trim(), null);
        }
        if (str.endsWith("-")) {
            return new HCPRange(null, str.substring(0, str.length() - 1).trim());
        }
        String[] parts = str.split("-");
        if (parts.length == 1) {
            return new HCPRange(parts[0].trim(), parts[0].trim());
        }
        if (parts.length != 2) {
            return null;
        }
        return new HCPRange(parts[0].trim(), parts[1].trim());
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

    static class BoundInfMin implements IBoundInference {
        final int min;

        public BoundInfMin(int min) {
            this.min = min;
        }

        @Override
        public boolean matches(Hand hand) {
            return hand.numHCP() >= min;
        }

        @Override
        public boolean negatable() {
            return true;
        }

        @Override
        public IBoundInference negate() {
            return new BoundInfMax(min - 1);
        }

        @Override
        public String toString() {
            return min + "+ hcp";
        }
    }

    static class BoundInfMax implements IBoundInference {
        final int max;

        public BoundInfMax(int max) {
            this.max = max;
        }

        @Override
        public boolean matches(Hand hand) {
            return hand.numHCP() <= max;
        }

        @Override
        public boolean negatable() {
            return true;
        }

        @Override
        public IBoundInference negate() {
            return new BoundInfMin(max + 1);
        }

        @Override
        public String toString() {
            return max + "- hcp";
        }
    }
}
