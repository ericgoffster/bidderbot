package bbidder.inferences.bound;

import java.util.Arrays;

import bbidder.Constants;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Range;
import bbidder.inferences.LikelySuitSummary;

public class CombinedTotalPointsBoundInf implements IBoundInference {
    final LikelySuitSummary[] tp;
    final Range r;
    
    public static IBoundInference createBounded(LikelySuitSummary[] tp, Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.bits == 0) {
            return ConstBoundInference.F;
        }
        return new CombinedTotalPointsBoundInf(tp, r);
    }
    
    static int getTotalPoints(Hand hand, LikelySuitSummary[] tp) {
        int pts = hand.totalPoints(Constants.NOTRUMP) + tp[Constants.NOTRUMP].minTotalPoints;
        for (int s = 0; s < 4; s++) {
            if (hand.numInSuit(s) + tp[s].minLength >= 8) {
                pts = Math.max(pts, hand.totalPoints(s) + tp[s].minTotalPoints);
            }
        }
        return pts > 40 ? 40 : pts;
    }

    @Override
    public int size() {
        return 1;
    }

    public CombinedTotalPointsBoundInf(LikelySuitSummary[] tp, Range r) {
        this.tp = tp;
        this.r = r;
    }

    @Override
    public boolean matches(Hand hand) {
        int tpts = getTotalPoints(hand, tp);
        return r.contains(tpts);
    }

    @Override
    public IBoundInference negate() {
        return new CombinedTotalPointsBoundInf(tp, r.not());
    }

    @Override
    public String toString() {
        return r + " tpts";
    }

    @Override
    public IBoundInference orReduce(IBoundInference i) {
        if (i instanceof CombinedTotalPointsBoundInf && Arrays.equals(tp, ((CombinedTotalPointsBoundInf) i).tp)) {
            return createBounded(tp, r.or(((CombinedTotalPointsBoundInf) i).r));
        }
        return null;
    }

    @Override
    public IBoundInference andReduce(IBoundInference i) {
        if (i instanceof CombinedTotalPointsBoundInf && Arrays.equals(tp, ((CombinedTotalPointsBoundInf) i).tp)) {
            return createBounded(tp, r.and(((CombinedTotalPointsBoundInf) i).r));
        }
        return null;
    }

}