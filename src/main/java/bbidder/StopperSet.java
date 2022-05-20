package bbidder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import bbidder.utils.BitUtil;

/**
 * Represents a set of Stopper's
 * 
 * @author goffster
 *
 */
public final class StopperSet implements Iterable<Stoppers> {
    public static final StopperSet ALL = new StopperSet(List.of(Stoppers.values()));
    public static final StopperSet NONE = new StopperSet(List.of());

    private final short stopperSet;

    private StopperSet(short stopperSet) {
        this.stopperSet = stopperSet;
        Optional<Integer> highest = BitUtil.highestBit(stopperSet);
        if (highest.isPresent() && highest.get() >= Stoppers.values().length) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Construct stoppers from a list of stoppers.
     * 
     * @param list
     *            The list of stoppers.
     */
    public StopperSet(Iterable<Stoppers> list) {
        this(createStoppers(list));
    }

    /**
     * Construct stoppers from a filter of stoppers.
     * 
     * @param filter
     *            The filter of stoppers.
     */
    public StopperSet(Predicate<Stoppers> filter) {
        this(createStoppers(filter));
    }

    /**
     * 
     * @param stoppers
     *            The stoppers to test
     * @return true if we contain the stopper
     */
    public boolean contains(Stoppers stoppers) {
        return (stopperSet & (1 << stoppers.ordinal())) != 0;
    }

    /**
     * @param other
     *            The other stopper set
     * @return this & other
     */
    public StopperSet and(StopperSet other) {
        return new StopperSet((short) (stopperSet & other.stopperSet));
    }

    /**
     * @param other
     *            The other stopper set
     * @return this |& other
     */
    public StopperSet or(StopperSet other) {
        return new StopperSet((short) (stopperSet | other.stopperSet));
    }

    /**
     * @return ~this
     */
    public StopperSet not() {
        return new StopperSet((short) (stopperSet ^ ALL.stopperSet));
    }

    /**
     * 
     * @param suit
     *            The suit to test.
     * @return True if we have definitely have a stopper in the given suit.
     */
    public boolean stopperIn(int suit) {
        for (Stoppers s : this) {
            if (!s.stopperIn(suit)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @param suit
     *            The suit to test.
     * @return True if we have definitely have no stopper in the given suit.
     */
    public boolean noStopperIn(int suit) {
        for (Stoppers s : this) {
            if (s.stopperIn(suit)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if we contain no stoppers.
     */
    public boolean isEmpty() {
        return stopperSet == 0;
    }

    /**
     * @return The number of stoppers
     */
    public int size() {
        return BitUtil.size(stopperSet);
    }

    /**
     * 
     * @return true if we contain all stoppers.
     */
    public boolean unBounded() {
        return stopperSet == ALL.stopperSet;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "none";
        }
        boolean[] haveStopper = { true, true, true, true };
        boolean[] haveNoStopper = { true, true, true, true };

        for (Stoppers stoppers : this) {
            for (int s = 0; s < 4; s++) {
                haveStopper[s] &= stoppers.stopperIn(s);
                haveNoStopper[s] &= !stoppers.stopperIn(s);
            }
        }
        List<String> strNoStoppers = new ArrayList<>();
        List<String> strStoppers = new ArrayList<>();
        for (int s = 0; s < 4; s++) {
            if (haveStopper[s]) {
                strStoppers.add(Strain.getName(s));
            }
            if (haveNoStopper[s]) {
                strNoStoppers.add(Strain.getName(s));
            }
        }
        if (strStoppers.size() == 0 && strNoStoppers.size() == 0) {
            return "unknown";
        }
        if (strStoppers.size() == 0) {
            return "not in " + String.join(",", strNoStoppers);
        }
        if (strNoStoppers.size() == 0) {
            return " in " + String.join(",", strStoppers);
        }
        return " in " + String.join(",", strStoppers) + ", not in " + String.join(",", strNoStoppers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopperSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StopperSet other = (StopperSet) obj;
        return Objects.equals(stopperSet, other.stopperSet);
    }

    @Override
    public Iterator<Stoppers> iterator() {
        return BitUtil.stream(stopperSet).mapToObj(i -> Stoppers.values()[i]).iterator();
    }

    private static short createStoppers(Iterable<Stoppers> list) {
        short stoppers = 0;
        for (Stoppers s : list) {
            stoppers |= 1 << s.ordinal();
        }
        return stoppers;
    }

    private static short createStoppers(Predicate<Stoppers> pred) {
        short stoppers = 0;
        for (Stoppers s : Stoppers.values()) {
            if (pred.test(s)) {
                stoppers |= 1 << s.ordinal();
            }
        }
        return stoppers;
    }
}
