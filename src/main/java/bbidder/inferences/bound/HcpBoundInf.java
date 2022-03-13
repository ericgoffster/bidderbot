package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.ShapeSet;

public class HcpBoundInf implements IBoundInference {
    final Range r;
    
    @Override
    public InfSummary getSummary() {
        return new InfSummary(ShapeSet.ALL, r);
    }
    
    @Override
    public InfSummary getNotSummary() {
        return new InfSummary(ShapeSet.ALL, r.not());
    }
    
    public HcpBoundInf(Range r) {
        this.r = r;
    }

    @Override
    public boolean matches(Hand hand) {
        return r.contains(hand.numHCP());
    }

    @Override
    public String toString() {
        return r + " hcp";
    }
}