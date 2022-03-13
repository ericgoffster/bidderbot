package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Range;

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
}