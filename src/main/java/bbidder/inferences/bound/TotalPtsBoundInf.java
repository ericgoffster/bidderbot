package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.inferences.LikelyHandSummary;

public class TotalPtsBoundInf implements IBoundInference {
    final LikelyHandSummary partnerSummary;
    final Range r;
    
    @Override
    public ShapeSet getShapes() {
        return ShapeSet.ALL;
    }
    
    @Override
    public ShapeSet getNotShapes() {
        return ShapeSet.ALL;
    }
    
    @Override
    public Range getHcp() {
        return Range.all(40);
    }
    
    @Override
    public Range getNotHcp() {
        return Range.all(40);
    }
    
    public static IBoundInference createBounded(LikelyHandSummary partnerSummary, Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.bits == 0) {
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