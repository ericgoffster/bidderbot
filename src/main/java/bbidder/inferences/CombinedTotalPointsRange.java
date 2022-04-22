package bbidder.inferences;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import bbidder.BiddingContext;
import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.SplitUtil;
import bbidder.inferences.bound.TotalPtsBoundInf;

/**
 * Represents the inference of the total points in a suit.
 * 
 * @author goffster
 *
 */
public class CombinedTotalPointsRange implements Inference {
    private static final Map<String, Integer> STD = Map.of("min", 18, "inv", 22, "gf", 25, "slaminv", 31, "slam", 33, "grandinv", 35, "grand", 37);
    public final Range rng;

    public CombinedTotalPointsRange(Integer min, Integer max) {
        super();
        this.rng = Range.between(min, max, 40);
    }

    public CombinedTotalPointsRange(Range rng) {
        super();
        this.rng = rng;
    }

    @Override
    public IBoundInference bind(Players players) {
        int tpts = players.partner.infSummary.minTotalPts();
        Range r = new Range(rng.bits >> tpts, 40);
        return TotalPtsBoundInf.create(players.partner.infSummary, r);
    }
    
    @Override
    public List<BiddingContext> resolveSuits(BiddingContext context) {
        return List.of(context.withInferenceAdded(this));
    }

    public static Range createRange(String str, Map<String, Integer> m) {
        final int dir;
        if (str.endsWith("+")) {
            str = str.substring(0, str.length() - 1);
            dir = 1;
        } else if (str.endsWith("-")) {
            str = str.substring(0, str.length() - 1);
            dir = -1;
        } else {
            dir = 0;
        }

        if (!m.containsKey(str)) {
            return null;
        }

        int pts = m.get(str);
        if (dir > 0) {
            return Range.between(pts, null, 40);
        }
        if (dir < 0) {
            return Range.between(null, pts - 1, 40);
        }

        Integer diff = null;
        for (var e : m.entrySet()) {
            if (e.getValue() > pts && (diff == null || e.getValue() - pts < diff)) {
                diff = e.getValue() - pts;
            }
        }

        if (diff == null) {
            return Range.between(pts, null, 40);
        }

        return Range.between(pts, pts + diff - 1, 40);
    }

    public static Range createRange(String str) {
        return createRange(str, STD);
    }

    public static CombinedTotalPointsRange makeRange(String str) {
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 1) {
            CombinedTotalPointsRange r1 = makeRange(parts[0]);
            CombinedTotalPointsRange r2 = makeRange(parts[1]);
            if (r1 == null || r2 == null) {
                return null;
            }
            return new CombinedTotalPointsRange(r1.rng.lowest(), r2.rng.highest());
        }
        Range createRange = createRange(str);
        if (createRange == null) {
            return null;
        }
        return new CombinedTotalPointsRange(createRange);
    }

    public static CombinedTotalPointsRange valueOf(String str) {
        if (str == null) {
            return null;
        }
        return makeRange(str.trim());
    }

    @Override
    public String toString() {
        return rng + " combined tpts";
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
