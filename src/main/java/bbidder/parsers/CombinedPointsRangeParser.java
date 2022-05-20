package bbidder.parsers;

import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;

import bbidder.PointRange;
import bbidder.utils.SplitUtil;

public class CombinedPointsRangeParser {
    public static final Map<String, Integer> POINT_RANGES = Map.of("min", 18, "inv", 22, "gf", 25, "slaminv", 31, "slam", 33, "grandinv", 35, "grand",
            37);
    
    public static String getCombinedLabel(int pts) {
        Entry<String, Integer> maxPts = null;
        for (var e : POINT_RANGES.entrySet()) {
            int nextMax = e.getValue();
            if (nextMax < pts && (maxPts == null || nextMax > maxPts.getValue())) {
                maxPts = e;
            }
        }
        return maxPts == null ? null : maxPts.getKey();
    }

    public static PointRange createCombinedTPtsRange(String str, Map<String, Integer> m) {
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

    public static PointRange parseCombinedTPtsRange(String str) {
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 1) {
            PointRange rlow = parseCombinedTPtsRange(parts[0]);
            PointRange rhigh = parseCombinedTPtsRange(parts[1]);
            if (rlow == null || rhigh == null) {
                return null;
            }
            OptionalInt l = rlow.lowest();
            OptionalInt h = rhigh.highest();
            if (!l.isPresent()) {
                return PointRange.NONE;
            }
            if (!h.isPresent()) {
                return PointRange.NONE;
            }
            int lower = l.getAsInt();
            int higher = h.getAsInt();
            return PointRange.between(lower, higher);
        }
        return CombinedPointsRangeParser.createCombinedTPtsRange(str, CombinedPointsRangeParser.POINT_RANGES);
    }
}
