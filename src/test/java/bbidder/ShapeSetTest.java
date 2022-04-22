package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import bbidder.ShapeSet.Stat;

public class ShapeSetTest {
    @Test
    public void testValueOf() {
        ShapeSet ss = ShapeSet.create(List.of(Shape._00030307, Shape._01000507));
        assertTrue(ss.contains(Shape._00030307));
        assertTrue(ss.contains(Shape._01000507));
        assertFalse(ss.contains(Shape._02000209));
        Stat[] stats = ss.getStats();
        assertEquals(stats[0].range, Range.between(0, 1, 13));
        assertEquals(stats[1].range, Range.none(13).add(0).add(3));
        assertEquals(stats[2].range, Range.none(13).add(3).add(5));
        assertEquals(stats[3].range, Range.none(13).add(7));

        assertTrue(ss.contains(Shape.getShape(1, 0, 5, 7)));
        assertFalse(ss.contains(Shape.getShape(2, 0, 5, 6)));

        ShapeSet ss2 = ShapeSet.create(List.of(Shape._00030307, Shape._05000404)).and(ss);
        assertTrue(ss2.contains(Shape._00030307));
        assertFalse(ss2.contains(Shape._01000507));
        assertFalse(ss2.contains(Shape._05000404));

        ShapeSet ss3 = ShapeSet.create(List.of(Shape._00030307, Shape._05000404)).or(ss);
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
