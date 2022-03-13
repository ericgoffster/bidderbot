package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.inferences.LikelyHandSummary;

public class CombinedTotalPointsBoundInf implements IBoundInference {
    final LikelyHandSummary partnerSummary;
    final Range r;
    
    @Override
    public IBoundInference negate() {
        return create(partnerSummary, r.not());
    }
    
    @Override
    public InfSummary getSummary() {
        return new InfSummary(ShapeSet.ALL, Range.all(40), r, Range.all(40));
    }
    
    @Override
    public IBoundInference andWith(InfSummary summary) {
        return create(partnerSummary, summary.ctpts.and(r));
    }
    
    public static IBoundInference create(LikelyHandSummary tp, Range r) {
        if (r.isEmpty()) {
            return ConstBoundInference.F;
        }
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        return new CombinedTotalPointsBoundInf(tp, r);
    }
    
    private CombinedTotalPointsBoundInf(LikelyHandSummary tp, Range r) {
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