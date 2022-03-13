package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a range of integers from 0 up to 64.
 * Handy for points, or number of cards in a suit.
 * 
 * @author goffster
 *
 */
public class Range {
    public final long bits;
    public final int max;

    public Range(long bits, int max) {
        super();
        this.bits = bits;
        this.max = max;
    }

    public int highest() {
        return BitUtil.highestBit(bits);
    }

    public int lowest() {
        return BitUtil.leastBit(bits);
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
        return new Range((1L << (n + 1)) - 1, max);
    }

    public static Range between(Integer lhs, Integer rhs, int max) {
        return atLeast(lhs, max).and(atMost(rhs, max));
    }

    public boolean unBounded() {
        return bits == ((1L << (max + 1)) - 1);
    }

    public Range not() {
        return new Range((~bits) & ((1L << (max + 1)) - 1), max);
    }

    public Range and(Range other) {
        return new Range(bits & other.bits, max);
    }

    public Range or(Range other) {
        return new Range(bits | other.bits, max);
    }

    public boolean contains(int pos) {
        if (unBounded()) {
            return true;
        }
        return (bits & (1L << pos)) != 0L;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bits, max);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Range other = (Range) obj;
        return bits == other.bits && max == other.max;
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
        if (ranges.size() == 1) {
            return ranges.get(0);
        }
        return "[" + String.join(",", ranges) + "]";
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
