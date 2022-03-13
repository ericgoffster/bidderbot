package bbidder;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ShapeSet implements Iterable<Shape> {
    public static final ShapeSet ALL = new ShapeSet(List.of(Shape.values()));
    public static final ShapeSet NONE = new ShapeSet(List.of());

    private final BitSet shapes;

    private ShapeSet(BitSet shapes) {
        this.shapes = shapes;
    }

    public ShapeSet(Iterable<Shape> list) {
        this.shapes = createShapes(list);
    }

    private static BitSet createShapes(Iterable<Shape> list) {
        BitSet shp = new BitSet(Shape.values().length);
        for (Shape s : list) {
            shp.set(s.ordinal());
        }
        return shp;
    }

    public ShapeSet(Predicate<Shape> pred) {
        this.shapes = createShapes(pred);
    }

    private static BitSet createShapes(Predicate<Shape> pred) {
        BitSet shp = new BitSet(Shape.values().length);
        for (Shape s : Shape.values()) {
            if (pred.test(s)) {
                shp.set(s.ordinal());
            }
        }
        return shp;
    }

    public boolean contains(Shape s) {
        return shapes.get(s.ordinal());
    }

    public ShapeSet and(ShapeSet other) {
        BitSet a = (BitSet) shapes.clone();
        a.and(other.shapes);
        return new ShapeSet(a);
    }

    public ShapeSet or(ShapeSet other) {
        BitSet o = (BitSet) shapes.clone();
        o.or(other.shapes);
        return new ShapeSet(o);
    }

    public ShapeSet not() {
        BitSet a = (BitSet) shapes.clone();
        a.xor(ALL.shapes);
        return new ShapeSet(a);
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

    @Override
    public String toString() {
        if (isEmpty()) {
            return "none";
        }
        short[] ranges = { 0, 0, 0, 0 };
        for (Shape shape : this) {
            for (int s = 0; s < 4; s++) {
                ranges[s] |= 1 << shape.numInSuit(s);
            }
        }
        List<String> str = new ArrayList<>();
        for (int s = 0; s < 4; s++) {
            Range rn = new Range(ranges[s], 13);
            if (rn.isEmpty()) {
                return "{}";
            }
            if (!rn.unBounded()) {
                str.add(rn + " " + Constants.STR_ALL_SUITS.charAt(s));
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

    public Range getSuit(int suit) {
        long bits = 0;
        for (Shape s : this) {
            bits |= (1L << s.numInSuit(suit));
        }
        return new Range(bits, 13);
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

    public double avgLenInSuit(int suit) {
        double tot = 0;
        double sum = 0;
        for (Shape s : this) {
            sum += s.numInSuit(suit) * s.p;
            tot += s.p;
        }
        return sum / tot;
    }
}
