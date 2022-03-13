package bbidder.inferences;

import bbidder.IBoundInference;

public class InfUtil {
    public static IBoundInference andReduce(IBoundInference a, IBoundInference b) {
        if (a == ConstBoundInference.F || b == ConstBoundInference.F) {
            return ConstBoundInference.F;
        }
        if (a == ConstBoundInference.T) {
            return b;
        }
        if (b == ConstBoundInference.T) {
            return a;
        }
        IBoundInference comb = a.andReduce(b);
        if (comb == null) {
            return b.andReduce(a);
        }
        return comb;
    }
    
    public static IBoundInference orReduce(IBoundInference a, IBoundInference b) {
        if (a == ConstBoundInference.T || b == ConstBoundInference.T) {
            return ConstBoundInference.T;
        }
        if (a == ConstBoundInference.F) {
            return b;
        }
        if (b == ConstBoundInference.F) {
            return a;
        }
        IBoundInference comb = a.orReduce(b);
        if (comb == null) {
            return b.orReduce(a);
        }
        return comb;
    }
}
