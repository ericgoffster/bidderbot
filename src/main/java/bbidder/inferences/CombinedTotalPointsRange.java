package bbidder.inferences;

import java.util.Arrays;
import java.util.Objects;

import bbidder.Constants;
import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.SplitUtil;

/**
 * Represents the inference of the total points in a suit.
 * 
 * @author goffster
 *
 */
public class CombinedTotalPointsRange implements Inference {
    public final Integer min;
    public final Integer max;

    public CombinedTotalPointsRange(Integer min, Integer max) {
        super();
        this.min = min;
        this.max = max;
    }

    static class Characteristic {
        final int minTotalPoints;
        final int minLength;

        public Characteristic(int minTotalPoints, int minLength) {
            super();
            this.minTotalPoints = minTotalPoints;
            this.minLength = minLength;
        }

        @Override
        public String toString() {
            return "minTotalPoints=" + minTotalPoints + ", minLength=" + minLength;
        }

        @Override
        public int hashCode() {
            return Objects.hash(minLength, minTotalPoints);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Characteristic other = (Characteristic) obj;
            return minLength == other.minLength && minTotalPoints == other.minTotalPoints;
        }
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        Characteristic[] tp = new Characteristic[5];
        for (int i = 0; i < 4; i++) {
            tp[i] = new Characteristic(context.likelyHands.partner.minTotalPoints(i), context.likelyHands.partner.minInSuit(i));
        }
        tp[4] = new Characteristic(context.likelyHands.partner.minTotalPoints(4), 0);
        Range r = Range.between(min, max, 40);
        return createBounded(tp, r);
    }

    private static IBoundInference createBounded(Characteristic[] tp, Range r) {
        if (r.unBounded()) {
            return ConstBoundInference.T;
        }
        if (r.bits == 0) {
            return ConstBoundInference.F;
        }
        return new BoundInf(tp, r);
    }

    public static CombinedTotalPointsRange makeRange(String str) {
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 1) {
            CombinedTotalPointsRange r1 = makeRange(parts[0]);
            CombinedTotalPointsRange r2 = makeRange(parts[1]);
            return new CombinedTotalPointsRange(r1.min, r2.max);
        }
        switch (str) {
        case "min":
            return new CombinedTotalPointsRange(19, 23);
        case "min+":
            return new CombinedTotalPointsRange(19, null);
        case "inv":
            return new CombinedTotalPointsRange(24, 25);
        case "inv+":
            return new CombinedTotalPointsRange(24, null);
        case "inv-":
            return new CombinedTotalPointsRange(null, 25);
        case "gf":
            return new CombinedTotalPointsRange(26, 30);
        case "gf+":
            return new CombinedTotalPointsRange(26, null);
        case "gf-":
            return new CombinedTotalPointsRange(null, 30);
        case "slaminv":
            return new CombinedTotalPointsRange(31, 32);
        case "slaminv+":
            return new CombinedTotalPointsRange(31, null);
        case "slaminv-":
            return new CombinedTotalPointsRange(null, 32);
        case "slam":
            return new CombinedTotalPointsRange(33, 36);
        case "slam+":
            return new CombinedTotalPointsRange(33, null);
        case "slam-":
            return new CombinedTotalPointsRange(null, 36);
        case "grand":
            return new CombinedTotalPointsRange(37, null);
        default:
            return null;
        }
    }

    public static CombinedTotalPointsRange valueOf(String str) {
        if (str == null) {
            return null;
        }
        return makeRange(str.trim());
    }

    private static int getTotalPoints(Hand hand, Characteristic[] tp) {
        int pts = hand.totalPoints(Constants.NOTRUMP) + tp[Constants.NOTRUMP].minTotalPoints;
        for (int s = 0; s < 4; s++) {
            if (hand.numInSuit(s) + tp[s].minLength >= 8) {
                pts = Math.max(pts, hand.totalPoints(s) + tp[s].minTotalPoints);
            }
        }
        return pts > 40 ? 40 : pts;
    }

    @Override
    public String toString() {
        if (max == null) {
            return min + "+ tpts";
        }
        if (min == null) {
            return max + "- tpts";
        }
        return min + "-" + max + " tpts";
    }

    @Override
    public int hashCode() {
        return Objects.hash(max, min);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CombinedTotalPointsRange other = (CombinedTotalPointsRange) obj;
        return Objects.equals(max, other.max) && Objects.equals(min, other.min);
    }

    static class BoundInf implements IBoundInference {
        final Characteristic[] tp;
        final Range r;

        public BoundInf(Characteristic[] tp, Range r) {
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
            return new BoundInf(tp, r.not());
        }

        @Override
        public String toString() {
            return r + " tpts";
        }

        @Override
        public IBoundInference orReduce(IBoundInference i) {
            if (i instanceof BoundInf && Arrays.equals(tp, ((BoundInf) i).tp)) {
                return createBounded(tp, r.or(((BoundInf) i).r));
            }
            return null;
        }

        @Override
        public IBoundInference andReduce(IBoundInference i) {
            if (i instanceof BoundInf && Arrays.equals(tp, ((BoundInf) i).tp)) {
                return createBounded(tp, r.and(((BoundInf) i).r));
            }
            return null;
        }

    }
}
