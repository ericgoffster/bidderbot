package bbidder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ShapeSetTest {
    @Test
    public void testValueOf() {
        ShapeSet ss = new ShapeSet(List.of(Shape._00030307, Shape._01000507));
        assertTrue(ss.contains(Shape._00030307));
        assertTrue(ss.contains(Shape._01000507));
        assertFalse(ss.contains(Shape._02000209));

        assertTrue(ss.contains(Shape.getShape(1, 0, 5, 7)));
        assertFalse(ss.contains(Shape.getShape(2, 0, 5, 6)));

        ShapeSet ss2 = new ShapeSet(List.of(Shape._00030307, Shape._05000404)).and(ss);
        assertTrue(ss2.contains(Shape._00030307));
        assertFalse(ss2.contains(Shape._01000507));
        assertFalse(ss2.contains(Shape._05000404));

        ShapeSet ss3 = new ShapeSet(List.of(Shape._00030307, Shape._05000404)).or(ss);
        assertTrue(ss3.contains(Shape._00030307));
        assertTrue(ss3.contains(Shape._01000507));
        assertTrue(ss3.contains(Shape._05000404));
        assertFalse(ss3.contains(Shape._02000209));

        ShapeSet ss4 = ss.not();
        assertFalse(ss4.contains(Shape._00030307));
        assertFalse(ss4.contains(Shape._01000507));
        assertTrue(ss4.contains(Shape._02000209));
    }
}
