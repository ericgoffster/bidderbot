package bbidder;

import java.util.BitSet;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

public class ShapeSet {
    public final BitSet shapes;

    public ShapeSet(BitSet shapes) {
        this.shapes = shapes;
    }

    public ShapeSet(Collection<Shape> list) {
        shapes = new BitSet(Shape.values().length);
        for(Shape s: list) {
            shapes.set(s.ordinal());
        }
    }
    
    public ShapeSet(Predicate<Shape> pred) {
        shapes = new BitSet(Shape.values().length);
        for(Shape s: Shape.values()) {
            if (pred.test(s)) {
                shapes.set(s.ordinal());
            }
        }
    }
    
    public boolean contains(Shape s) {
        return shapes.get(s.ordinal());
    }
    
    public ShapeSet and(ShapeSet other) {
        BitSet a = (BitSet)shapes.clone();
        a.and(other.shapes);
        return new ShapeSet(a);
    }
    
    public ShapeSet or(ShapeSet other) {
        BitSet o = (BitSet)shapes.clone();
        o.or(other.shapes);
        return new ShapeSet(o);
    }
    
    public ShapeSet not() {
        return new ShapeSet(shape -> !contains(shape));
    }

    public boolean isEmpty() {
        return shapes.isEmpty();
    }

    public boolean isFull() {
        return not().isEmpty();
    }

    @Override
    public String toString() {
        int[] min = {13,13,13,13};
        int[] max = {0,0,0,0};
        for (int i = shapes.nextSetBit(0); i != -1; i = shapes.nextSetBit(i + 1)) {
            Shape shape = Shape.values()[i];
            for(int s = 0; s < 4; s++) {
                min[s] = Math.min(min[s], shape.numInSuit(s));
                max[s] = Math.max(max[s], shape.numInSuit(s));
            }
        }
        String[] str = new String[4];
        for(int s = 0; s < 4; s++) {
            if (min[s] == max[s]) {
                str[s] = min[s] + "" + Constants.STR_ALL_SUITS.charAt(s);
            } else {
                str[s] = min[s] + "-" + max[s] + Constants.STR_ALL_SUITS.charAt(s);
            }
        }
        return String.join(",",  str);
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
    
    
}
