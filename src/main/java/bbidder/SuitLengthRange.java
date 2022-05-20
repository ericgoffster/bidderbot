package bbidder;

import java.util.Optional;

import bbidder.utils.BitUtil;

public class SuitLengthRange extends Range {
    public static final int MAX = 13;
    public static final SuitLengthRange NONE = new SuitLengthRange(0);
    public static final SuitLengthRange ALL = new SuitLengthRange(bitMask(MAX));
    
    public SuitLengthRange(long bits) {
        super(bits, MAX);
    }

    public Optional<Integer> highest() {
        return BitUtil.highestBit(bits);
    }

    public Optional<Integer> lowest() {
        return BitUtil.leastBit(bits);
    }

    public static SuitLengthRange exactly(int n) {
        if (n < 0 || n > MAX) {
            throw new IllegalArgumentException();
        }
        return new SuitLengthRange(1 << n);
    }

    public static SuitLengthRange atLeast(Integer n) {
        if (n == null) {
            return ALL;
        }
        if (n < 0 || n > MAX) {
            throw new IllegalArgumentException();
        }
        return new SuitLengthRange(~bitMask(n - 1) & bitMask(MAX));
    }

    public static SuitLengthRange atMost(Integer n) {
        if (n == null) {
            return ALL;
        }
        if (n < 0 || n > MAX) {
            throw new IllegalArgumentException();
        }
        return new SuitLengthRange(bitMask(n));
    }

    public SuitLengthRange add(int pos) {
        if (pos < 0 || pos > max) {
            throw new IllegalArgumentException();
        }
        return new SuitLengthRange(bits | (1L << pos));
    }

    public static SuitLengthRange between(Integer lhs, Integer rhs) {
        return atLeast(lhs).and(atMost(rhs));
    }

    public SuitLengthRange not() {
        return new SuitLengthRange((~bits) & bitMask(MAX));
    }

    public SuitLengthRange and(SuitLengthRange other) {
        return new SuitLengthRange(bits & other.bits);
    }

    public SuitLengthRange or(SuitLengthRange other) {
        return new SuitLengthRange(bits | other.bits);
    }

}
