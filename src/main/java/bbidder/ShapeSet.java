package bbidder;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.function.Predicate;

/**
 * Represents a set of shapes.
 * @author goffster
 *
 */
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

    /**
     * @param list the collection of shapes.
     * @return a ShapeSet from a collection of shapes.
     */
    public static ShapeSet create(Iterable<Shape> list) {
        return createNew(createShapes(list));
    }

    /**
     * @param filter the filter of shapes
     * @return a ShapeSet from a shape predicate.
     */
    public static ShapeSet create(Predicate<Shape> filter) {
        BitSet a = createShapes(filter);
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

    /**
     * @param shape The shape
     * @return true if the ShapeSet contains the shape.
     */
    public boolean contains(Shape shape) {
        return shapes.get(shape.ordinal());
    }

    /**
     * @param other The other shape
     * @return this shapeset & other shapeset
     */
    public ShapeSet and(ShapeSet other) {
        BitSet a = (BitSet) shapes.clone();
        a.and(other.shapes);
        return createNew(a);
    }

    /**
     * @param other The other shape
     * @return this shapeset | other shapeset
     */
    public ShapeSet or(ShapeSet other) {
        BitSet a = (BitSet) shapes.clone();
        a.or(other.shapes);
        return createNew(a);
    }

    /**
     * @return ~(this shapeset)
     */
    public ShapeSet not() {
        BitSet a = (BitSet) shapes.clone();
        a.xor(ALL.shapes);
        return createNew(a);
    }

    /**
     * @return true if this ShapeSet has no shapes.
     */
    public boolean isEmpty() {
        return shapes.isEmpty();
    }

    /**
     * @return The number of shapes in this set.
     */
    public int size() {
        return shapes.cardinality();
    }

    /**
     * @return true If this ShapeSet contains all shape sets.
     */
    public boolean unBounded() {
        return size() == Shape.values().length;
    }
    
    private static final ShapeStats EMPTY_STATS[] = { ShapeStats.EMPTY, ShapeStats.EMPTY, ShapeStats.EMPTY, ShapeStats.EMPTY};

    /**
     * @return The stats on each suit
     */
    public ShapeStats[] getStats() {
        if (isEmpty()) {
            return EMPTY_STATS;
        }
        long[] bits = new long[4];
        double tot = 0;
        double[] sum = new double[4];
        for (Shape s : this) {
            bits[0] |= (1L << s.numInSuit(0));
            bits[1] |= (1L << s.numInSuit(1));
            bits[2] |= (1L << s.numInSuit(2));
            bits[3] |= (1L << s.numInSuit(3));
            double probability = s.getProbability();
            sum[0] += s.numInSuit(0) * probability;
            sum[1] += s.numInSuit(1) * probability;
            sum[2] += s.numInSuit(2) * probability;
            sum[3] += s.numInSuit(3) * probability;
            tot += probability;
        }
        return new ShapeStats[] {
            new ShapeStats(new SuitLengthRange(bits[0]), OptionalDouble.of(sum[0] / tot)),
            new ShapeStats(new SuitLengthRange(bits[1]), OptionalDouble.of(sum[1] / tot)),
            new ShapeStats(new SuitLengthRange(bits[2]), OptionalDouble.of(sum[2] / tot)),
            new ShapeStats(new SuitLengthRange(bits[3]), OptionalDouble.of(sum[3] / tot))};
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "none";
        }
        ShapeStats[] stats = getStats();
        List<String> str = new ArrayList<>();
        for (int s = 0; s < 4; s++) {
            SuitLengthRange rn = stats[s].lengths;
            if (rn.isEmpty()) {
                return "{}";
            }
            if (!rn.unBounded()) {
                str.add(Strain.getName(s) + "-" + stats[s]);
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
        return shapes.stream().mapToObj(i -> Shape.values()[i]).iterator();
    }
}
