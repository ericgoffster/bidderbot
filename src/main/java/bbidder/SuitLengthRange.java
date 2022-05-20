package bbidder;

import bbidder.utils.BitUtil;

/**
 * Represents a set of suit lengths (0-13)
 * 
 * @author goffster
 *
 */
public final class SuitLengthRange extends Range {
    public static final int MAX = 13;
    public static final SuitLengthRange NONE = new SuitLengthRange(0);
    public static final SuitLengthRange ALL = new SuitLengthRange(BitUtil.bitMask(MAX));

    @Override
    public int max() {
        return MAX;
    }

    public SuitLengthRange(long bits) {
        super(bits);
    }

    public static SuitLengthRange exactly(int n) {
        if (n < 0 || n > MAX) {
            throw new IllegalArgumentException();
        }
        return new SuitLengthRange(1 << n);
    }

    public static SuitLengthRange atLeast(int n) {
        if (n == 0) {
            return ALL;
        }
        if (n == MAX) {
            return NONE;
        }
        if (n < 0 || n >= MAX) {
            throw new IllegalArgumentException();
        }
        return new SuitLengthRange(BitUtil.bitMask(MAX) ^ BitUtil.bitMask(n - 1));
    }

    public static SuitLengthRange atMost(int n) {
        if (n == MAX) {
            return ALL;
        }
        if (n == 0) {
            return NONE;
        }
        if (n < 0 || n >= MAX) {
            throw new IllegalArgumentException();
        }
        return new SuitLengthRange(BitUtil.bitMask(n));
    }

    public SuitLengthRange add(int pos) {
        if (pos < 0 || pos > MAX) {
            throw new IllegalArgumentException();
        }
        return new SuitLengthRange(bits | (1L << pos));
    }

    public static SuitLengthRange between(Integer lhs, Integer rhs) {
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

    public SuitLengthRange not() {
        return new SuitLengthRange(BitUtil.bitMask(MAX) ^ bits);
    }

    public SuitLengthRange and(SuitLengthRange other) {
        return new SuitLengthRange(bits & other.bits);
    }

    public SuitLengthRange or(SuitLengthRange other) {
        return new SuitLengthRange(bits | other.bits);
    }

}
