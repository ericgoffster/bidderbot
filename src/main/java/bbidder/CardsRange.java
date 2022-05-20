package bbidder;

import java.util.Optional;

import bbidder.utils.BitUtil;

/**
 * Represents a set of particular cards.
 * @author goffster
 *
 */
public final class CardsRange extends Range {
    public static final int MAX = 13;
    public static final CardsRange NONE = new CardsRange(0);
    public static final CardsRange ALL = new CardsRange(BitUtil.bitMask(MAX));
    
    @Override
    public int max() {
        return MAX;
    }
    
    public CardsRange(long bits) {
        super(bits);
    }

    public Optional<Integer> highest() {
        return BitUtil.highestBit(bits);
    }

    public Optional<Integer> lowest() {
        return BitUtil.leastBit(bits);
    }

    public static CardsRange exactly(int n) {
        if (n < 0 || n > MAX) {
            throw new IllegalArgumentException();
        }
        return new CardsRange(1 << n);
    }

    public static CardsRange atLeast(int n) {
        if (n == 0) {
            return ALL;
        }
        if (n == MAX) {
            return NONE;
        }
        if (n < 1 || n >= MAX) {
            throw new IllegalArgumentException();
        }
        return new CardsRange(BitUtil.bitMask(MAX) ^ BitUtil.bitMask(n - 1));
    }

    public static CardsRange atMost(int n) {
        if (n == MAX) {
            return ALL;
        }
        if (n == 0) {
            return NONE;
        }
        if (n < 1 || n >= MAX) {
            throw new IllegalArgumentException();
        }
        return new CardsRange(BitUtil.bitMask(n));
    }

    public CardsRange add(int pos) {
        if (pos < 0 || pos > MAX) {
            throw new IllegalArgumentException();
        }
        return new CardsRange(bits | (1L << pos));
    }

    public static CardsRange between(Integer lhs, Integer rhs) {
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

    public CardsRange not() {
        return new CardsRange(BitUtil.bitMask(MAX) ^ bits);
    }

    public CardsRange and(CardsRange other) {
        return new CardsRange(bits & other.bits);
    }

    public CardsRange or(CardsRange other) {
        return new CardsRange(bits | other.bits);
    }

}
