package bbidder.inferences.bound;

import bbidder.Constants;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Range;
import bbidder.inferences.LikelyHandSummary;

public class CombinedTotalPointsBoundInf implements IBoundInference {
    final LikelyHandSummary partnerSummary;
    final Range r;
    
    public static IBoundInference createBounded(LikelyHandSummary partnerSummary, Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.bits == 0) {
            return ConstBoundInference.F;
        }
        return new CombinedTotalPointsBoundInf(partnerSummary, r);
    }
    
    static int getTotalPoints(Hand hand, LikelyHandSummary partnerSummary) {
        int pts = hand.totalPoints(Constants.NOTRUMP) + partnerSummary.suitSummaries[Constants.NOTRUMP].minTotalPoints;
        for (int s = 0; s < 4; s++) {
            if (hand.numInSuit(s) + partnerSummary.suitSummaries[s].minLength >= 8) {
                pts = Math.max(pts, hand.totalPoints(s) + partnerSummary.suitSummaries[s].minTotalPoints);
            }
        }
        return pts > 40 ? 40 : pts;
    }

    @Override
    public int size() {
        return 1;
    }

    public CombinedTotalPointsBoundInf(LikelyHandSummary tp, Range r) {
        this.partnerSummary = tp;
        this.r = r;
    }

    @Override
    public boolean matches(Hand hand) {
        int tpts = getTotalPoints(hand, partnerSummary);
        return r.contains(tpts);
    }

    @Override
    public IBoundInference negate() {
        return new CombinedTotalPointsBoundInf(partnerSummary, r.not());
    }

    @Override
    public String toString() {
        return r + " tpts";
    }

    @Override
    public IBoundInference orReduce(IBoundInference i) {
        if (i instanceof CombinedTotalPointsBoundInf && partnerSummary.equals(((CombinedTotalPointsBoundInf) i).partnerSummary)) {
            return createBounded(partnerSummary, r.or(((CombinedTotalPointsBoundInf) i).r));
        }
        return null;
    }

    @Override
    public IBoundInference andReduce(IBoundInference i) {
        if (i instanceof CombinedTotalPointsBoundInf && partnerSummary.equals(((CombinedTotalPointsBoundInf) i).partnerSummary)) {
            return createBounded(partnerSummary, r.and(((CombinedTotalPointsBoundInf) i).r));
        }
        return null;
    }

}