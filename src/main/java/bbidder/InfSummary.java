package bbidder;

import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * Represents the summary of inferences.
 * 
 * @author goffster
 *
 */
public final class InfSummary {
    public static final InfSummary ALL = new InfSummary(ShapeSet.ALL, PointRange.ALL, PointRange.ALL, StopperSet.ALL, StopperSet.ALL);
    public static final InfSummary NONE = new InfSummary(ShapeSet.NONE, PointRange.NONE, PointRange.NONE, StopperSet.NONE, StopperSet.NONE);

    public final ShapeSet shape;
    public final PointRange tpts;
    public final PointRange hcp;
    public final StopperSet stoppers;
    public final StopperSet partialStoppers;

    public final AtomicReference<ShapeStats[]> shapeStats;

    public InfSummary(ShapeSet shape, PointRange tpts, PointRange hcp, StopperSet stoppers, StopperSet partialStoppers) {
        super();
        this.shape = shape;
        this.tpts = tpts;
        this.hcp = hcp;
        this.stoppers = stoppers;
        this.partialStoppers = partialStoppers;
        this.shapeStats = new AtomicReference<>();
    }

    public InfSummary withShapes(ShapeSet shape) {
        return new InfSummary(shape, tpts, hcp, stoppers, partialStoppers);
    }

    public InfSummary withStoppers(StopperSet stoppers) {
        return new InfSummary(shape, tpts, hcp, stoppers, partialStoppers);
    }

    public InfSummary withPartialStoppers(StopperSet partialStoppers) {
        return new InfSummary(shape, tpts, hcp, stoppers, partialStoppers);
    }

    public InfSummary withTotalPoints(PointRange tpts) {
        return new InfSummary(shape, tpts, hcp, stoppers, partialStoppers);
    }

    public InfSummary withHcp(PointRange hcp) {
        return new InfSummary(shape, tpts, hcp, stoppers, partialStoppers);
    }

    public InfSummary and(InfSummary other) {
        return new InfSummary(shape.and(other.shape), tpts.and(other.tpts), hcp.and(other.hcp), stoppers.and(other.stoppers),
                partialStoppers.and(other.partialStoppers));
    }

    public InfSummary or(InfSummary other) {
        return new InfSummary(shape.or(other.shape), tpts.or(other.tpts), hcp.or(other.hcp), stoppers.or(other.stoppers),
                partialStoppers.or(other.partialStoppers));
    }

    @Override
    public String toString() {
        return shape + "," + tpts + " tpts" + hcp + " hcp" + ",stoppers:" + stoppers + ",partial stoppers:" + partialStoppers;
    }

    /**
     * @return true if this summary represents the empty set of possible hands.
     */
    public boolean isEmpty() {
        return shape.isEmpty() || tpts.isEmpty() || hcp.isEmpty() || stoppers.isEmpty() || partialStoppers.isEmpty();
    }

    /**
     * @param suit
     *            The suit
     * @return The range of possible lengths for the suit
     */
    public SuitLengthRange getSuit(int suit) {
        return getShapeStats(suit).lengths;
    }

    /**
     * @param suit
     *            The suit
     * @return The statistics on the given suit.
     */
    public ShapeStats getShapeStats(int suit) {
        return shapeStats.updateAndGet(st -> (st != null) ? st : shape.getStats())[suit];
    }

    /**
     * @return The minimum total points, empty if there is an empty set of points.
     */
    public OptionalInt minTotalPts() {
        return tpts.lowest();
    }

    /**
     * 
     * @param suit
     *            The suit
     * @return The minimum length in a suit, empty if there is an empty set of lengths.
     */
    public OptionalInt minLenInSuit(int suit) {
        return getSuit(suit).lowest();
    }

    /**
     * 
     * @param suit
     *            The suit
     * @return The average length in a suit, empty if there is an empty set of lengths.
     */
    public OptionalDouble avgLenInSuit(int suit) {
        return getShapeStats(suit).averageLength;
    }

    /**
     * @return A bit pattern of all suits that have been bid. empty if
     *         the suits are the empty set.
     */
    public OptionalInt getBidSuits() {
        int[] minL = IntStream.range(0, 4).mapToObj(suit -> minLenInSuit(suit)).filter(o -> o.isPresent()).mapToInt(o -> o.getAsInt()).toArray();
        if (minL.length != 4) {
            return OptionalInt.empty();
        }
        double[] avgL = IntStream.range(0, 4).mapToDouble(suit -> avgLenInSuit(suit).getAsDouble()).toArray();
        if (avgL.length != 4) {
            return OptionalInt.empty();
        }
        Integer[] suitArr = { 0, 1, 2, 3 };
        Arrays.sort(suitArr, (s1, s2) -> -Double.compare(minL[s1], minL[s2]));
        int min = minL[suitArr[3]];
        int suits = 0;
        for (int i = 0; i < 4; i++) {
            if (avgL[suitArr[i]] >= 3 && minL[suitArr[i]] > min) {
                suits |= 1 << suitArr[i];
            }
        }
        return OptionalInt.of(suits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partialStoppers, shape, stoppers, tpts, hcp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InfSummary other = (InfSummary) obj;
        return Objects.equals(partialStoppers, other.partialStoppers) && Objects.equals(shape, other.shape)
                && Objects.equals(stoppers, other.stoppers) && Objects.equals(tpts, other.tpts) && Objects.equals(hcp, other.hcp);
    }
}
