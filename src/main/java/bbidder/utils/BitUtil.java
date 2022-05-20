package bbidder.utils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

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
    private static final int highest[];
    private static final int lowest[];

    static {
        set = new int[65536][];
        highest = new int[65536];
        lowest = new int[65536];
        int[] res = new int[16];
        for (int i = 0; i < 65536; i++) {
            int j = i;
            int pos = 0;
            int len = 0;
            int h = -1;
            int l = -1;
            while (j != 0) {
                if (j % 2 != 0) {
                    res[len++] = pos;
                    if (h < 0 || pos > h) {
                        h = pos;
                    }
                    if (l < 0 || pos < l) {
                        l = pos;
                    }
                }
                j >>= 1;
                pos++;
            }
            set[i] = Arrays.copyOf(res, len);
            highest[i] = h;
            lowest[i] = l;
        }
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return Number of bits set in the pattern.
     */
    public static int size(short pattern) {
        return set[pattern & 0xffff].length;
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return Number of bits set in the pattern.
     */
    public static int size(long pattern) {
        return
                set[(int)(pattern & 0xffff)].length +
                set[(int)((pattern >>> 16) & 0xffff)].length +
                set[(int)((pattern >>> 32) & 0xffff)].length +
                set[(int)((pattern >>> 48) & 0xffff)].length;
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return The index of the highest bit set in the pattern.
     */
    public static Optional<Integer> highestBit(short pattern) {
        return pattern == 0 ? Optional.empty() : Optional.of(highest[pattern & 0xffff]);
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return The index of the highest bit set in the pattern.
     */
    public static Optional<Integer> leastBit(short pattern) {
        return pattern == 0 ? Optional.empty() : Optional.of(lowest[pattern & 0xffff]);
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return The index of the highest bit set in the pattern.
     */
    public static Optional<Integer> highestBit(long pattern) {
        int p3 = (int) ((pattern >>> 48) & 0xffff);
        if (p3 != 0) {
            return Optional.of(highest[p3] + 48);
        }
        int p2 = (int) ((pattern >>> 32) & 0xffff);
        if (p2 != 0) {
            return Optional.of(highest[p2] + 32);
        }
        int p1 = (int) ((pattern >>> 16) & 0xffff);
        if (p1 != 0) {
            return Optional.of(highest[p1] + 16);
        }
        int p0 = (int) (pattern & 0xffff);
        if (p0 != 0) {
            return Optional.of(highest[p0]);
        }
        return Optional.empty();
    }

    /**
     * @param pattern
     *            The bit pattern
     * @return The index of the lowest bit set in the pattern. (-1 if all zero)
     */
    public static Optional<Integer> leastBit(long pattern) {
        int p0 = (int) (pattern & 0xffff);
        if (p0 != 0) {
            return Optional.of(lowest[p0]);
        }
        int p1 = (int) ((pattern >>> 16) & 0xffff);
        if (p1 != 0) {
            return Optional.of(lowest[p1] + 16);
        }
        int p2 = (int) ((pattern >>> 32) & 0xffff);
        if (p2 != 0) {
            return Optional.of(lowest[p2] + 32);
        }
        int p3 = (int) ((pattern >>> 48) & 0xffff);
        if (p3 != 0) {
            return Optional.of(lowest[p3] + 48);
        }
        return Optional.empty();
    }
    
    public static IntStream stream(long pattern) {
        IntStream stream = IntStream.empty();
        int p0 = (int) (pattern & 0xffff);
        if (p0 != 0) {
            stream = IntStream.concat(stream, Arrays.stream(set[p0]));
        }
        int p1 = (int) ((pattern >>> 16) & 0xffff);
        if (p1 != 0) {
            stream = IntStream.concat(stream, Arrays.stream(set[p1]).map(i -> i + 16));
        }
        int p2 = (int) ((pattern >>> 32) & 0xffff);
        if (p2 != 0) {
            stream = IntStream.concat(stream, Arrays.stream(set[p2]).map(i -> i + 32));
        }
        int p3 = (int) ((pattern >>> 48) & 0xffff);
        if (p3 != 0) {
            stream = IntStream.concat(stream, Arrays.stream(set[p3]).map(i -> i + 48));
        }
        return stream;
    }

    public static IntStream stream(short pattern) {
        return pattern == 0 ? IntStream.empty() : Arrays.stream(set[pattern & 0xffff]);
    }

    public static long bitMask(int max) {
        return (1L << (max + 1)) - 1;
    }
}
