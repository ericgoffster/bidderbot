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
    
    public static long bitMask(int max) {
        return (1L << (max + 1)) - 1;
    }

    public Range(long bits, int max) {
        super();
        if ((bits & ~bitMask(max)) != 0) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        this.max = max;
    }

    public boolean isEmpty() {
        return bits == 0;
    }

    public int highest() {
        return BitUtil.highestBit(bits);
    }

    public int lowest() {
        int leastBit = BitUtil.leastBit(bits);
        return leastBit < 0 ? max + 1 : leastBit;
    }

    public static Range all(int max) {
        return new Range(bitMask(max), max);
    }

    public static Range none(int max) {
        return new Range(0, max);
    }

    public static Range atLeast(Integer n, int max) {
        if (n == null) {
            return all(max);
        }
        if (n < 0 || n > max) {
            throw new IllegalArgumentException();
        }
        return new Range(~bitMask(n - 1) & bitMask(max), max);
    }

    public static Range atMost(Integer n, int max) {
        if (n == null) {
            return all(max);
        }
        if (n < 0 || n > max) {
            throw new IllegalArgumentException();
        }
        return new Range(bitMask(n), max);
    }

    public static Range between(Integer lhs, Integer rhs, int max) {
        return atLeast(lhs, max).and(atMost(rhs, max));
    }

    public boolean unBounded() {
        return bits == bitMask(max);
    }

    public Range not() {
        return new Range((~bits) & bitMask(max), max);
    }

    public Range and(Range other) {
        if (max != other.max) {
            throw new IllegalArgumentException();
        }
        return new Range(bits & other.bits, max);
    }

    public Range or(Range other) {
        if (max != other.max) {
            throw new IllegalArgumentException();
        }
        return new Range(bits | other.bits, max);
    }

    public boolean contains(int pos) {
        if (pos < 0 || pos > max) {
            throw new IllegalArgumentException();
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
