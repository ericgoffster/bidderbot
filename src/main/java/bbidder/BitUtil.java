package bbidder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Fast bit utilities.
 * 
 * @author goffster
 *
 */
public class BitUtil {
    private static int set[][] = new int[65536][];

    static {
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

    public static int size(int pattern) {
        return size(toLong(pattern));
    }

    private static long toLong(int pattern) {
        return pattern & 0xffffffffL;
    }

    public static int size(short pattern) {
        return size(toLong(pattern));
    }

    private static long toLong(short pattern) {
        return pattern & 0xffffL;
    }

    public static int size(byte pattern) {
        return size(toLong(pattern));
    }

    private static long toLong(byte pattern) {
        return pattern & 0xffL;
    }

    public static int size(long pattern) {
        int sz = 0;
        long patt = pattern;
        while (patt != 0) {
            sz += set[(int) (patt & 0xffff)].length;
            patt >>>= 16;
        }
        return sz;
    }

    public static int highestBit(byte pattern) {
        return highestBit(toLong(pattern));
    }

    public static int highestBit(short pattern) {
        return highestBit(toLong(pattern));
    }

    public static int highestBit(int pattern) {
        return highestBit(toLong(pattern));
    }

    public static int highestBit(long pattern) {
        if (pattern == 0) {
            return -1;
        }
        return 63 - Long.numberOfLeadingZeros(pattern);
    }

    public static int leastBit(long pattern) {
        if (pattern == 0) {
            return -1;
        }
        return Long.numberOfTrailingZeros(pattern);
    }

    public static Iterable<Integer> iterate(long pattern) {
        return new PatternIterable(pattern);
    }

    public static Iterable<Integer> iterate(int pattern) {
        return iterate(toLong(pattern));
    }

    public static Iterable<Integer> iterate(short pattern) {
        return iterate(toLong(pattern));
    }

    public static Iterable<Integer> iterate(byte pattern) {
        return iterate(toLong(pattern));
    }

    private static final class PatternIterable implements Iterable<Integer> {
        private final long pattern;

        private PatternIterable(long pattern) {
            this.pattern = pattern;
        }

        @Override
        public Iterator<Integer> iterator() {
            return new PatternIterator(pattern);
        }
    }

    private static final class PatternIterator implements Iterator<Integer> {
        int j = 0;
        long patt;
        int[] res;
        int i = 0;

        public PatternIterator(long pattern) {
            patt = pattern >>> 16;
            res = set[(int) (pattern & 0xffff)];
        }

        @Override
        public boolean hasNext() {
            if (i < res.length) {
                return true;
            }
            return patt != 0;
        }

        @Override
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
    }

}
