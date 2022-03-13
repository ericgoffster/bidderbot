package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.ShapeSet;

public class HcpBoundInf implements IBoundInference {
    final Range r;
    
    @Override
    public IBoundInference negate() {
        return create(r.not());
    }  

    public static IBoundInference create(Range r) {
        if (r.isEmpty()) {
            return ConstBoundInference.F;
        }
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        return new HcpBoundInf(r);
    }
    
    @Override
    public InfSummary getSummary() {
        return new InfSummary(ShapeSet.ALL, r, Range.all(40));
    }
    
    private HcpBoundInf(Range r) {
        this.r = r;
    }
    
    @Override
    public IBoundInference andWith(InfSummary summary) {
        return create(r.and(summary.hcp));
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