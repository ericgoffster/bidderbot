package bbidder.inferences;

import java.util.ArrayList;
import java.util.List;

import bbidder.BitUtil;

public class Range {
    public final long bits;
    public final int max;

    public Range(long bits, int max) {
        super();
        this.bits = bits;
        this.max = max;
    }

    public static Range atLeast(Integer n, int max) {
        if (n == null) {
            return new Range((1L << (max + 1)) - 1, max);
        }
        return atMost(n - 1, max).not();
    }

    public static Range atMost(Integer n, int max) {
        if (n == null) {
            return new Range((1L << (max + 1)) - 1, max);
        }
        return new Range((1 << (n + 1)) - 1, max);
    }

    public static Range between(Integer lhs, Integer rhs, int max) {
        Range atl = atLeast(lhs, max);
        Range atm = atMost(rhs, max);
        return new Range(atl.bits & atm.bits, max);
    }
    
    public boolean unBounded() {
        return bits == ((1L << max) - 1);
    }

    public Range not() {
        return new Range((~bits) & ((1L << (max + 1)) - 1), max);
    }

    public boolean contains(int pos) {
        return (bits & (1L << pos)) != 0;
    }

    @Override
    public String toString() {
        List<String> ranges = new ArrayList<>();
        Integer lhs = null;
        Integer rhs = null;
        for (int i : BitUtil.iterate(bits)) {
            if (rhs == null || lhs == null) {
                lhs = i;
                rhs = i;
            } else if (i == rhs + 1) {
                rhs = i;
            } else {
                ranges.add(getRangeItem(lhs, rhs));
                lhs = i;
                rhs = i;
            }
        }
        if (lhs != null && rhs != null) {
            ranges.add(getRangeItem(lhs, rhs));
        }
        return String.join("|", ranges);
    }

    private String getRangeItem(Integer lhs, Integer rhs) {
        if (lhs.intValue() == rhs.intValue()) {
            return String.valueOf(lhs);
        } else {
            if (lhs.intValue() == 0) {
                return rhs + "-";
            } else if (rhs.intValue() == max) {
                return lhs + "+";
            } else {
                return lhs + "-" + rhs;
            }
        }
    }
}
