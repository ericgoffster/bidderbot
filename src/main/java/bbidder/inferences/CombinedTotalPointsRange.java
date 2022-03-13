package bbidder.inferences;

import java.util.Objects;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.InferenceContext;
import bbidder.Range;
import bbidder.SplitUtil;
import bbidder.inferences.bound.CombinedTotalPointsBoundInf;

/**
 * Represents the inference of the total points in a suit.
 * 
 * @author goffster
 *
 */
public class CombinedTotalPointsRange implements Inference {
    public final Range rng;

    public CombinedTotalPointsRange(Integer min, Integer max) {
        super();
        this.rng = Range.between(min, max, 40);
    }

    @Override
    public IBoundInference bind(InferenceContext context) {
        Characteristic[] tp = new Characteristic[5];
        for (int i = 0; i < 4; i++) {
            tp[i] = new Characteristic(context.likelyHands.partner.minTotalPoints(i), context.likelyHands.partner.minInSuit(i));
        }
        tp[4] = new Characteristic(context.likelyHands.partner.minTotalPoints(4), 0);
        return CombinedTotalPointsBoundInf.createBounded(tp, rng);
    }

    public static CombinedTotalPointsRange makeRange(String str) {
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 1) {
            CombinedTotalPointsRange r1 = makeRange(parts[0]);
            CombinedTotalPointsRange r2 = makeRange(parts[1]);
            return new CombinedTotalPointsRange(r1.rng.lowest(), r2.rng.highest());
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

    @Override
    public String toString() {
        return rng + " tpts";
    }

    @Override
    public int hashCode() {
        return Objects.hash(rng);
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
        return Objects.equals(rng, other.rng);
    }
}
