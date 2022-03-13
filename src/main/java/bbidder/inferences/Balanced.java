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
        return new BoundInf(true);
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

    private static final class BoundInf implements IBoundInference {
        final boolean bal;

        public BoundInf(boolean bal) {
            super();
            this.bal = bal;
        }

        @Override
        public boolean matches(Hand hand) {
            return isBalanced(hand) == bal;
        }

        @Override
        public IBoundInference negate() {
            return new BoundInf(!bal);
        }

        @Override
        public String toString() {
            return bal ? "balanced" : "unbalanced";
        }

        @Override
        public IBoundInference orReduce(IBoundInference i) {
            if (i instanceof BoundInf) {
                if (bal == ((BoundInf) i).bal) {
                    return this;
                }
                if (bal != ((BoundInf) i).bal) {
                    return ConstBoundInference.T;
                }
                return this;
            }
            return null;
        }

        @Override
        public IBoundInference andReduce(IBoundInference i) {
            if (i instanceof BoundInf) {
                if (bal == ((BoundInf) i).bal) {
                    return this;
                }
                if (bal != ((BoundInf) i).bal) {
                    return ConstBoundInference.F;
                }
                return this;
            }
            if (i instanceof SuitRange.BoundInf) {
                if (((SuitRange.BoundInf) i).r.highest() <= 1 || ((SuitRange.BoundInf) i).r.highest() >= 6) {
                    if (bal) {
                        return ConstBoundInference.F;
                    } else {
                        return i;
                    }
                }
            }
            if (i instanceof OpeningPreempt.BoundInf) {
                if (bal) {
                    return ConstBoundInference.F;
                } else {
                    return i;
                }
            }
            return null;
        }
    }
}
