package bbidder;

import java.util.Optional;

import bbidder.utils.BitUtil;

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

    public Optional<Integer> highest() {
        return BitUtil.highestBit(bits);
    }

    public Optional<Integer> lowest() {
        return BitUtil.leastBit(bits);
    }

    public static PointRange exactly(int n) {
        if (n < 0 || n > MAX) {
            throw new IllegalArgumentException();
        }
        return new PointRange(1 << n);
    }

    public static PointRange atLeast(Integer n) {
        if (n == null) {
            return ALL;
        }
        if (n < 0 || n > MAX) {
            throw new IllegalArgumentException();
        }
        return new PointRange(~BitUtil.bitMask(n - 1) & BitUtil.bitMask(MAX));
    }

    public static PointRange atMost(Integer n) {
        if (n == null) {
            return ALL;
        }
        if (n < 0 || n > MAX) {
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
        return atLeast(lhs).and(atMost(rhs));
    }

    public PointRange not() {
        return new PointRange((~bits) & BitUtil.bitMask(MAX));
    }

    public PointRange and(PointRange other) {
        return new PointRange(bits & other.bits);
    }

    public PointRange or(PointRange other) {
        return new PointRange(bits | other.bits);
    }

}
