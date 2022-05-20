package bbidder;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public final class ShapeSet implements Iterable<Shape> {
    public static final ShapeSet ALL = new ShapeSet(Shape.values().length);
    public static final ShapeSet NONE = new ShapeSet(0);

    private final BitSet shapes;

    private ShapeSet(BitSet shapes) {
        this.shapes = shapes;
    }

    private ShapeSet(int n) {
        this.shapes = new BitSet(n);
        for (int i = 0; i < n; i++) {
            this.shapes.set(i);
        }
    }

    private static BitSet createShapes(Iterable<Shape> list) {
        BitSet shapes = new BitSet(Shape.values().length);
        for (Shape s : list) {
            shapes.set(s.ordinal());
        }
        return shapes;
    }

    public static ShapeSet create(Iterable<Shape> list) {
        return createNew(createShapes(list));
    }

    public static ShapeSet create(Predicate<Shape> pred) {
        BitSet a = createShapes(pred);
        return createNew(a);
    }

    private static ShapeSet createNew(BitSet bs) {
        if (bs.cardinality() == Shape.values().length) {
            return ALL;
        }
        if (bs.cardinality() == 0) {
            return NONE;
        }
        return new ShapeSet(bs);
    }

    private static BitSet createShapes(Predicate<Shape> pred) {
        BitSet shapes = new BitSet(Shape.values().length);
        for (Shape s : Shape.values()) {
            if (pred.test(s)) {
                shapes.set(s.ordinal());
            }
        }
        return shapes;
    }

    public boolean contains(Shape s) {
        return shapes.get(s.ordinal());
    }

    public ShapeSet and(ShapeSet other) {
        BitSet a = (BitSet) shapes.clone();
        a.and(other.shapes);
        return createNew(a);
    }

    public ShapeSet or(ShapeSet other) {
        BitSet a = (BitSet) shapes.clone();
        a.or(other.shapes);
        return createNew(a);
    }

    public ShapeSet not() {
        BitSet a = (BitSet) shapes.clone();
        a.xor(ALL.shapes);
        return createNew(a);
    }

    public boolean isEmpty() {
        return shapes.isEmpty();
    }

    public int size() {
        return shapes.cardinality();
    }

    public boolean unBounded() {
        return size() == Shape.values().length;
    }

    public Stat[] getStats() {
        long[] bits = new long[4];
        double tot = 0;
        double[] sum = new double[4];
        for (Shape s : this) {
            bits[0] |= (1L << s.numInSuit(0));
            bits[1] |= (1L << s.numInSuit(1));
            bits[2] |= (1L << s.numInSuit(2));
            bits[3] |= (1L << s.numInSuit(3));
            sum[0] += s.numInSuit(0) * s.p;
            sum[1] += s.numInSuit(1) * s.p;
            sum[2] += s.numInSuit(2) * s.p;
            sum[3] += s.numInSuit(3) * s.p;
            tot += s.p;
        }
        if (tot == 0) {
            Stat[] stats = new Stat[4];
            stats[0] = new Stat(0, Range.none(13), Optional.empty());
            stats[1] = new Stat(1, Range.none(13), Optional.empty());
            stats[2] = new Stat(2, Range.none(13), Optional.empty());
            stats[3] = new Stat(3, Range.none(13), Optional.empty());
            return stats;
        }
        Stat[] stats = new Stat[4];
        stats[0] = new Stat(0, new Range(bits[0], 13), Optional.of(sum[0] / tot));
        stats[1] = new Stat(1, new Range(bits[1], 13), Optional.of(sum[1] / tot));
        stats[2] = new Stat(2, new Range(bits[2], 13), Optional.of(sum[2] / tot));
        stats[3] = new Stat(3, new Range(bits[3], 13), Optional.of(sum[3] / tot));
        return stats;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "none";
        }
        Stat[] stats = getStats();
        List<String> str = new ArrayList<>();
        for (int s = 0; s < 4; s++) {
            Range rn = stats[s].range;
            if (rn.isEmpty()) {
                return "{}";
            }
            if (!rn.unBounded()) {
                str.add(stats[s].toString());
            }
        }
        if (str.size() == 0) {
            return "almost any shape";
        }
        return String.join(",", str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shapes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShapeSet other = (ShapeSet) obj;
        return Objects.equals(shapes, other.shapes);
    }

    @Override
    public Iterator<Shape> iterator() {
        return new Iterator<>() {
            int i = shapes.nextSetBit(0);

            @Override
            public boolean hasNext() {
                return i >= 0;
            }

            @Override
            public Shape next() {
                Shape shape = Shape.values()[i];
                i = shapes.nextSetBit(i + 1);
                return shape;
            }

        };
    }

    public static class Stat {
        final int suit;
        final Range range;
        final Optional<Double> avg;

        public Stat(int suit, Range range, Optional<Double> avg) {
            super();
            this.suit = suit;
            this.range = range;
            this.avg = avg;
        }

        @Override
        public String toString() {
            if (!avg.isPresent()) {
                return "undefined " + Strain.getName(suit);
            }
            return range + " " + Strain.getName(suit) + " average " + avg;
        }
    }
}
