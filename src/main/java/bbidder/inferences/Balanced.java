package bbidder.inferences;

import bbidder.BitUtil;
import bbidder.InferenceContext;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;

/**
 * Represents the inference of a "balanced" hand
 * 
 * @author goffster
 *
 */
public class Balanced implements Inference {
    private static final BalancedBoundInf BALANCED = new BalancedBoundInf();

    private static boolean isBalanced(Hand hand) {
        int ndoub = 0;
        for (int s = 0; s < 4; s++) {
            int len = BitUtil.size(hand.suits[s]);
            if (len < 2) {
                return false;
            }
            if (len == 2) {
                ndoub++;
            }
        }
        return ndoub <= 1;
    }

    public Balanced() {
        super();
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        return BALANCED;
    }

    public static Balanced valueOf(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("balanced")) {
            return new Balanced();
        }
        return null;
    }

    @Override
    public String toString() {
        return "balanced";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

    private static final class BalancedBoundInf implements IBoundInference {
        @Override
        public boolean matches(Hand hand) {
            return isBalanced(hand);
        }

        @Override
        public boolean negatable() {
            return true;
        }

        @Override
        public IBoundInference negate() {
            return new UnbalancedBoundInf();
        }

        @Override
        public String toString() {
            return "balanced";
        }
    }

    private static final class UnbalancedBoundInf implements IBoundInference {
        private static final UnbalancedBoundInf UNBALANCED = new UnbalancedBoundInf();

        @Override
        public boolean matches(Hand hand) {
            return !isBalanced(hand);
        }

        @Override
        public boolean negatable() {
            return true;
        }

        @Override
        public IBoundInference negate() {
            return UNBALANCED;
        }

        @Override
        public String toString() {
            return "unbalanced";
        }
    }
}
