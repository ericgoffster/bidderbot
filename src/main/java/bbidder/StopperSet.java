package bbidder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class StopperSet implements Iterable<Stoppers> {
    public static final StopperSet ALL = new StopperSet(List.of(Stoppers.values()));
    public static final StopperSet NONE = new StopperSet(List.of());

    private final short stoppers;

    private StopperSet(short stoppers) {
        this.stoppers = stoppers;
        int highest = BitUtil.highestBit(stoppers);
        if (highest >= Stoppers.values().length) {
            throw new IllegalArgumentException();
        }
    }

    public StopperSet(Iterable<Stoppers> list) {
        this(createStoppers(list));
    }

    private static short createStoppers(Iterable<Stoppers> list) {
        short stoppers = 0;
        for (Stoppers s : list) {
            stoppers |= 1 << s.ordinal();
        }
        return stoppers;
    }

    public StopperSet(Predicate<Stoppers> pred) {
        this(createStoppers(pred));
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

    public boolean contains(Stoppers s) {
        return (stoppers & (1 << s.ordinal())) != 0;
    }

    public StopperSet and(StopperSet other) {
        short stoppers2 = (short) (stoppers & other.stoppers);
        return new StopperSet(stoppers2);
    }

    public StopperSet or(StopperSet other) {
        return new StopperSet((short) (stoppers | other.stoppers));
    }

    public StopperSet not() {
        return new StopperSet((short) (stoppers ^ ALL.stoppers));
    }

    public boolean isEmpty() {
        return stoppers == 0;
    }

    public int size() {
        return BitUtil.size(stoppers);
    }

    public boolean unBounded() {
        return stoppers == ALL.stoppers;
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
                strStoppers.add("" + Constants.STR_ALL_SUITS.charAt(s));
            }
            if (haveNoStopper[s]) {
                strNoStoppers.add("" + Constants.STR_ALL_SUITS.charAt(s));
            }
        }
        if (strStoppers.size() == 0 && strNoStoppers.size() == 0) {
            return "stoppers unknown";
        }
        if (strStoppers.size() == 0) {
            return "no stoppers in " + String.join(",", strNoStoppers);
        }
        if (strNoStoppers.size() == 0) {
            return "stoppers in " + String.join(",", strStoppers);
        }
        return "stoppers in " + String.join(",", strStoppers) + ", no stoppers in " + String.join(",", strNoStoppers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stoppers);
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
        return Objects.equals(stoppers, other.stoppers);
    }

    public boolean stopperIn(int suit) {
        for (Stoppers s : this) {
            if (!s.stopperIn(suit)) {
                return false;
            }
        }
        return true;
    }

    public boolean noStopperIn(int suit) {
        for (Stoppers s : this) {
            if (s.stopperIn(suit)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<Stoppers> iterator() {
        Iterable<Integer> iterable = BitUtil.iterate(stoppers);
        return new Iterator<>() {
            Iterator<Integer> i = iterable.iterator();

            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @Override
            public Stoppers next() {
                return Stoppers.values()[i.next()];
            }

        };
    }
}
