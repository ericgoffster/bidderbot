package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import bbidder.utils.BitUtil;

/**
 * Represents a range of integers from 0 up to 64.
 * Handy for points, or number of cards in a suit.
 * 
 * @author goffster
 *
 */
public abstract class Range {
    public final long bits;
    public abstract int max();

    public Range(long bits) {
        super();
        if ((bits & ~BitUtil.bitMask(max())) != 0) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
    }

    public boolean isEmpty() {
        return bits == 0;
    }

    public Optional<Integer> highest() {
        return BitUtil.highestBit(bits);
    }

    public Optional<Integer> lowest() {
        return BitUtil.leastBit(bits);
    }

    public boolean unBounded() {
        return bits == BitUtil.bitMask(max());
    }

    public boolean contains(int pos) {
        if (pos < 0 || pos > max()) {
            throw new IllegalArgumentException();
        }
        return (bits & (1L << pos)) != 0L;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bits);
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
        return bits == other.bits;
    }
    
    private class State {
        List<String> ranges = new ArrayList<>();
        Integer lhs = null;
        Integer rhs = null;
        
        public void add(int i) {
            if (rhs == null || lhs == null) {
                lhs = i;
                rhs = i;
            } else if (i == rhs + 1) {
                rhs = i;
            } else {
                ranges.add(getRangeItem());
                lhs = i;
                rhs = i;
            }
        }      

        private String getRangeItem() {
            if (lhs.intValue() == rhs.intValue()) {
                return String.valueOf(lhs);
            } else {
                if (lhs.intValue() == 0) {
                    return rhs + "-";
                } else if (rhs.intValue() == max()) {
                    return lhs + "+";
                } else {
                    return lhs + "-" + rhs;
                }
            }
        }
        
        @Override
        public String toString() {
            if (lhs != null && rhs != null) {
                ranges.add(getRangeItem());
            }
            if (ranges.size() == 1) {
                return ranges.get(0);
            }
            return "[" + String.join(",", ranges) + "]";
        }
    }
    
    @Override
    public String toString() {
        State state = new State();
        BitUtil.stream(bits).forEach(state::add);
        return state.toString();
    }
}
