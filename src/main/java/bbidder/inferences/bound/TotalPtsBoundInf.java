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
        return createBounded(partnerSummary, r.not());
    }  

    @Override
    public InfSummary getSummary() {
        return new InfSummary(ShapeSet.ALL, Range.all(40), Range.all(40), r);
    }
    
    @Override
    public InfSummary getNotSummary() {
        return new InfSummary(ShapeSet.ALL, Range.all(40), Range.all(40), r.not());
    }
    
    @Override
    public IBoundInference andWith(InfSummary summary) {
        return createBounded(partnerSummary, r.and(summary.tpts));
    }
    
    public static IBoundInference createBounded(LikelyHandSummary partnerSummary, Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.isEmpty()) {
            return ConstBoundInference.F;
        }
        return new TotalPtsBoundInf(partnerSummary, r);
    }
    
    public TotalPtsBoundInf(LikelyHandSummary tp, Range r) {
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