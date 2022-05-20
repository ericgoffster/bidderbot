package bbidder;

import bbidder.utils.BitUtil;

/**
 * Represents a set of possible points (0 - 40)
 * 
 * @author goffster
 *
 */
public final class PointRange extends Range {
    public static final int MAX = 40;
    public static final PointRange NONE = new PointRange(0);
    public static final PointRange ALL = new PointRange(BitUtil.bitMask(MAX));

    @Override
    public int max() {
        return MAX;
    }

    public PointRange(long bits) {
        super(bits);
    }

    public static PointRange exactly(int n) {
        if (n < 0 || n > MAX) {
            throw new IllegalArgumentException();
        }
        return new PointRange(1 << n);
    }

    public static PointRange atLeast(int n) {
        if (n == 0) {
            return ALL;
        }
        if (n == MAX) {
            return NONE;
        }
        if (n < 1 || n >= MAX) {
            throw new IllegalArgumentException();
        }
        return new PointRange(BitUtil.bitMask(MAX) ^ BitUtil.bitMask(n - 1));
    }

    public static PointRange atMost(int n) {
        if (n == MAX) {
            return ALL;
        }
        if (n == 0) {
            return NONE;
        }
        if (n < 1 || n >= MAX) {
            throw new IllegalArgumentException();
        }
        return new PointRange(BitUtil.bitMask(n));
    }

    public PointRange add(int pos) {
        if (pos < 0 || pos > MAX) {
            throw new IllegalArgumentException();
        }
        return new PointRange(bits | (1L << pos));
    }

    public static PointRange between(Integer lhs, Integer rhs) {
        if (lhs == null) {
            if (rhs == null) {
                return ALL;
            }
            return atMost(rhs);
        }
        if (rhs == null) {
            return atLeast(lhs);
        }
        return atLeast(lhs).and(atMost(rhs));
    }

    public PointRange not() {
        return new PointRange(BitUtil.bitMask(MAX) ^ bits);
    }

    public PointRange and(PointRange other) {
        return new PointRange(bits & other.bits);
    }

    public PointRange or(PointRange other) {
        return new PointRange(bits | other.bits);
    }

}
