package bbidder.inferences.bound;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Range;
import bbidder.inferences.LikelyHandSummary;

public class TotalPtsBoundInf implements IBoundInference {
    final LikelyHandSummary partnerSummary;
    final Range r;
    
    public static IBoundInference createBounded(LikelyHandSummary partnerSummary, Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.bits == 0) {
            return ConstBoundInference.F;
        }
        return new TotalPtsBoundInf(partnerSummary, r);
    }
    
    @Override
    public int size() {
        return 1;
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
    public IBoundInference negate() {
        return new TotalPtsBoundInf(partnerSummary, r.not());
    }

    @Override
    public String toString() {
        return r + " tpts" + ", partner=" + partnerSummary;
    }
}