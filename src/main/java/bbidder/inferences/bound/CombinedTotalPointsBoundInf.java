package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.inferences.LikelyHandSummary;

public class CombinedTotalPointsBoundInf implements IBoundInference {
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
    
    public static IBoundInference createBounded(LikelyHandSummary partnerSummary, Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.bits == 0) {
            return ConstBoundInference.F;
        }
        return new CombinedTotalPointsBoundInf(partnerSummary, r);
    }
    
    public CombinedTotalPointsBoundInf(LikelyHandSummary tp, Range r) {
        this.partnerSummary = tp;
        this.r = r;
    }

    @Override
    public boolean matches(Hand hand) {
        return r.contains(hand.getCombinedTotalPoints(partnerSummary));
    }

    @Override
    public String toString() {
        return r + " combined tpts" + ", partner=" + partnerSummary;
    }
}