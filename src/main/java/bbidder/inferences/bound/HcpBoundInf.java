package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Range;
import bbidder.inferences.HCPRange;

public class HcpBoundInf implements IBoundInference {
    final Range r;
    
    @Override
    public int size() {
        return 1;
    }

    public HcpBoundInf(Range r) {
        this.r = r;
    }

    @Override
    public boolean matches(Hand hand) {
        return r.contains(hand.numHCP());
    }

    @Override
    public IBoundInference negate() {
        return new HcpBoundInf(r.not());
    }

    @Override
    public String toString() {
        return r + " hcp";
    }

    @Override
    public IBoundInference andReduce(IBoundInference i) {
        if (i instanceof HcpBoundInf) {
            return HCPRange.createBound(r.and(((HcpBoundInf) i).r));
        }
        return null;
    }

    @Override
    public IBoundInference orReduce(IBoundInference i) {
        if (i instanceof HcpBoundInf) {
            return HCPRange.createBound(r.or(((HcpBoundInf) i).r));
        }
        return null;
    }
}