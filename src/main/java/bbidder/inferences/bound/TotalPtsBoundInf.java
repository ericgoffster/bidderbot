package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.inferences.LikelyHandSummary;

public class TotalPtsBoundInf implements IBoundInference {
    final LikelyHandSummary partnerSummary;
    final Range r;
    
    @Override
    public IBoundInference negate() {
        return create(partnerSummary, r.not());
    }  

    @Override
    public InfSummary getSummary() {
        return new InfSummary(ShapeSet.ALL, Range.all(40), Range.all(40), r);
    }
    
    @Override
    public IBoundInference andWith(InfSummary summary) {
        return create(partnerSummary, r.and(summary.tpts));
    }
    
    public static IBoundInference create(LikelyHandSummary partnerSummary, Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.isEmpty()) {
            return ConstBoundInference.F;
        }
        return new TotalPtsBoundInf(partnerSummary, r);
    }
    
    private TotalPtsBoundInf(LikelyHandSummary tp, Range r) {
        this.partnerSummary = tp;
        this.r = r;
    }

    @Override
    public boolean matches(Hand hand) {
        return r.contains(hand.getTotalPoints(partnerSummary));
    }

    @Override
    public String toString() {
        return r + " tpts" + ", partner=" + partnerSummary;
    }
}