package bbidder.inferences;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import bbidder.IBoundInference;
import bbidder.Inference;
import bbidder.Players;
import bbidder.Range;
import bbidder.SuitTable;
import bbidder.inferences.bound.ConstBoundInference;
import bbidder.inferences.bound.TotalPtsBoundInf;
import bbidder.utils.SplitUtil;

/**
 * Represents the inference of the total points in a suit.
 * 
 * @author goffster
 *
 */
public final class CombinedTotalPointsRange extends Inference {
    private static final Map<String, Integer> STD = Map.of("min", 18, "inv", 22, "gf", 25, "slaminv", 31, "slam", 33, "grandinv", 35, "grand", 37);
    private final Range rng;

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
        return players.partner.infSummary.minTotalPts()
                .map(tpts -> TotalPtsBoundInf.create(players.partner.infSummary, new Range(rng.bits >> tpts, 40)))
                .orElse(ConstBoundInference.F);
    }

    @Override
    public Stream<Context> resolveSuits(SuitTable suitTable) {
        return Stream.of(new Context(suitTable));
    }

    public static CombinedTotalPointsRange makeRange(String str) {
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 1) {
            CombinedTotalPointsRange rlow = makeRange(parts[0]);
            CombinedTotalPointsRange rhigh = makeRange(parts[1]);
            if (rlow == null || rhigh == null) {
                return null;
            }
            Optional<Integer> l = rlow.rng.lowest();
            Optional<Integer> h = rhigh.rng.highest();
            Range createRange = l.map(lower -> h.map(higher -> Range.between(lower, higher, 40)).orElse(Range.none(40))).orElse(Range.none(40));
            return new CombinedTotalPointsRange(createRange);
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

    private static Range createRange(String str, Map<String, Integer> m) {
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
            return Range.between(pts, null, 40);
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
            return Range.between(pts, null, 40);
        }

        return Range.between(pts, maxPts, 40);
    }

    private static Range createRange(String str) {
        return createRange(str, STD);
    }
}
