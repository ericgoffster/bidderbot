package bbidder.inferences;

import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.PointRange;
import bbidder.SuitTable;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.TotalPtsBoundInf;

/**
 * Represents the inference of the total points in a suit.
 * 
 * @author goffster
 *
 */
public final class CombinedTotalPointsRange extends Inference {
    private static final Map<String, Integer> STD = Map.of("min", 18, "inv", 22, "gf", 25, "slaminv", 31, "slam", 33, "grandinv", 35, "grand", 37);
    public final PointRange rng;

    public CombinedTotalPointsRange(Integer min, Integer max) {
        super();
        this.rng = PointRange.between(min, max);
    }

    public CombinedTotalPointsRange(PointRange rng) {
        super();
        this.rng = rng;
    }

    @Override
    public IBoundInference bind(Players players) {
        OptionalInt minTotalPts = players.partner.infSummary.minTotalPts();
        if (!minTotalPts.isPresent()) {
            return ConstBoundInference.F;
        }
        int tpts = minTotalPts.getAsInt();
        return TotalPtsBoundInf.create(players.partner.infSummary, new PointRange(rng.bits >> tpts));
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return Stream.of(new Context(suitTable));
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

    private static PointRange createRange(String str, Map<String, Integer> m) {
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

        Integer pts = m.get(str);
        if (dir > 0) {
            return PointRange.between(pts, null);
        }

        Integer maxPts = null;
        for (var e : m.entrySet()) {
            int nextMax = e.getValue() - 1;
            if (nextMax >= pts && (maxPts == null || nextMax < maxPts)) {
                maxPts = nextMax;
            }
        }

        if (dir < 0) {
            pts = null;
        }

        if (maxPts == null) {
            return PointRange.between(pts, null);
        }

        return PointRange.between(pts, maxPts);
    }

    public static PointRange createRange(String str) {
        return createRange(str, STD);
    }
}
