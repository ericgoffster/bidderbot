package bbidder.utils;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * Fast bit utilities.
 * 
 * @author goffster
 *
 */
public final class BitUtil {
    /**
     * set[patt] = array of set bits
     */
    private static final int set[][];

    static {
        set = new int[65536][];
        int[] res = new int[16];
        for (int i = 0; i < 65536; i++) {
            int j = i;
            int pos = 0;
            int len = 0;
            while (j != 0) {
                if (j % 2 != 0) {
                    res[len++] = pos;
                }
                j >>= 1;
                pos++;
            }
            set[i] = Arrays.copyOf(res, len);
        }
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return Number of bits set in the pattern.
     */
    public static int size(short pattern) {
        return size(toLong(pattern));
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return Number of bits set in the pattern.
     */
    public static int size(long pattern) {
        int sz = 0;
        long patt = pattern;
        while (patt != 0) {
            sz += set[(int) (patt & 0xffff)].length;
            patt >>>= 16;
        }
        return sz;
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return The index of the highest bit set in the pattern.
     */
    public static Optional<Integer> highestBit(short pattern) {
        return highestBit(toLong(pattern));
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return The index of the highest bit set in the pattern.
     */
    public static Optional<Integer> highestBit(long pattern) {
        if (pattern == 0) {
            return Optional.empty();
        }
        return Optional.of(63 - Long.numberOfLeadingZeros(pattern));
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return The index of the lowest bit set in the pattern. (-1 if all zero)
     */
    public static Optional<Integer> leastBit(long pattern) {
        if (pattern == 0) {
            return Optional.empty();
        }
        return Optional.of(Long.numberOfTrailingZeros(pattern));
    }
    
    public static IntStream stream(long pattern) {
        return StreamSupport.stream(new PatternIterator(pattern), false).mapToInt(i -> i.intValue());
    }

    public static IntStream stream(short pattern) {
        return stream(toLong(pattern));
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return The long version of the pattern.
     */
    private static long toLong(short pattern) {
        return pattern & 0xffffL;
    }

    private static final class PatternIterator implements Spliterator<Integer> {
        int j = 0;
        long patt;
        int[] res;
        int i = 0;

        public PatternIterator(long pattern) {
            patt = pattern >>> 16;
            res = set[(int) (pattern & 0xffff)];
        }

        public boolean hasNext() {
            if (i < res.length) {
                return true;
            }
            return patt != 0;
        }

        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            while (i >= res.length && patt != 0) {
                res = set[(int) (patt & 0xffff)];
                i = 0;
                j += 16;
                patt >>>= 16;
            }
            return j + res[i++];
        }

        @Override
        public boolean tryAdvance(Consumer<? super Integer> action) {
            if (hasNext()) {
                action.accept(next());
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<Integer> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return 0;
        }

        @Override
        public int characteristics() {
            return 0;
        }
    }

}
