package bbidder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import bbidder.ShapeSet.Stat;

/**
 * Represents the summary of inferences.
 * 
 * @author goffster
 *
 */
public final class InfSummary {
    public static final InfSummary ALL = new InfSummary(ShapeSet.ALL, Range.all(40), StopperSet.ALL, StopperSet.ALL);
    public static final InfSummary NONE = new InfSummary(ShapeSet.NONE, Range.none(40), StopperSet.NONE, StopperSet.NONE);

    public final ShapeSet shape;
    public final Range tpts;
    public final StopperSet stoppers;
    public final StopperSet partialStoppers;

    public final AtomicReference<Stat[]> stats;

    public InfSummary(ShapeSet shape, Range tpts, StopperSet stoppers, StopperSet partialStoppers) {
        super();
        this.shape = shape;
        this.tpts = tpts;
        this.stoppers = stoppers;
        this.partialStoppers = partialStoppers;
        this.stats = new AtomicReference<>();
    }

    public InfSummary withShapes(ShapeSet shape) {
        return new InfSummary(shape, tpts, stoppers, partialStoppers);
    }

    public InfSummary withStoppers(StopperSet stoppers) {
        return new InfSummary(shape, tpts, stoppers, partialStoppers);
    }

    public InfSummary withPartialStoppers(StopperSet partialStoppers) {
        return new InfSummary(shape, tpts, stoppers, partialStoppers);
    }

    public InfSummary withTotalPoints(Range tpts) {
        return new InfSummary(shape, tpts, stoppers, partialStoppers);
    }

    public InfSummary and(InfSummary other) {
        return new InfSummary(shape.and(other.shape), tpts.and(other.tpts), stoppers.and(other.stoppers), partialStoppers.and(other.partialStoppers));
    }

    public InfSummary or(InfSummary other) {
        return new InfSummary(shape.or(other.shape), tpts.or(other.tpts), stoppers.or(other.stoppers), partialStoppers.or(other.partialStoppers));
    }

    @Override
    public String toString() {
        return shape + "," + tpts + " tpts" + ",stoppers:" + stoppers + ",partial stoppers:" + partialStoppers;
    }

    public boolean isEmpty() {
        return shape.isEmpty() || tpts.isEmpty() || stoppers.isEmpty() || partialStoppers.isEmpty();
    }

    public Range getSuit(int suit) {
        return getStat(suit).range;
    }

    public Stat getStat(int suit) {
        return stats.updateAndGet(st -> (st != null) ? st : shape.getStats())[suit];
    }

    public Optional<Integer> minTotalPts() {
        return tpts.lowest();
    }

    public Optional<Integer> minLenInSuit(int suit) {
        return getSuit(suit).lowest();
    }

    public Optional<Double> avgLenInSuit(int suit) {
        return getStat(suit).avg;
    }

    public short getBidSuits() {
        Integer[] suitArr = { 0, 1, 2, 3 };
        List<Optional<Integer>> minLens = IntStream.range(0, 4).mapToObj(suit -> minLenInSuit(suit)).collect(Collectors.toList());
        if (!minLens.get(0).isPresent() || !minLens.get(1).isPresent() || !minLens.get(2).isPresent() || !minLens.get(3).isPresent()) {
            return 0;
        }
        Arrays.sort(suitArr, (s1, s2) -> -Double.compare(avgLenInSuit(s1).get(), avgLenInSuit(s2).get()));
        List<Integer> minL = minLens.stream().map(ml -> ml.get()).collect(Collectors.toList());
        if (minL.get(suitArr[0]) >= 5 || minL.get(suitArr[0]) >= 4 && minL.get(suitArr[1]) >= 4) {
            short suits = 0;
            for (int i = 0; i < 4 && minL.get(suitArr[i]) >= 4; i++) {
                suits |= (short) (1 << suitArr[i]);
            }
            return suits;
        }
        if (minL.get(suitArr[0]) == 4) {
            if (minL.get(suitArr[1]) > minL.get(suitArr[2])) {
                return (short) ((1 << suitArr[0]) | (1 << suitArr[1]));
            }
            return (short) ((1 << suitArr[0]));
        }
        {
            short suits = 0;
            for (int i = 0; i < 4 && avgLenInSuit(suitArr[i]).get() >= 4; i++) {
                suits |= (short) (1 << suitArr[i]);
            }
            return suits;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(partialStoppers, shape, stoppers, tpts);
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
                && Objects.equals(stoppers, other.stoppers) && Objects.equals(tpts, other.tpts);
    }
}
