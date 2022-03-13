package bbidder;

import java.util.BitSet;
import java.util.Collection;

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
        BitSet n = new BitSet(Shape.values().length);
        Shape[] allShapes = Shape.values();
        for(int i = 0; i < allShapes.length; i++) {
            if (!contains(allShapes[i])) {
                n.set(i);
            }
        }
        return new ShapeSet(n);
    }

    public boolean isEmpty() {
        return shapes.isEmpty();
    }

    public boolean isFull() {
        return not().isEmpty();
    }
}
