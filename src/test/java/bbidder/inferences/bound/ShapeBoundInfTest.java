package bbidder.inferences.bound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import bbidder.Hand;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.StopperSet;

public class ShapeBoundInfTest {
    @Test
    public void test() {
        assertEquals(ShapeBoundInf.create(ShapeSet.ALL), ConstBoundInference.T);
        assertEquals(ShapeBoundInf.create(ShapeSet.NONE), ConstBoundInference.F);
        assertTrue(ShapeBoundInf.create(ShapeSet.create(List.of(Shape._00030307, Shape._01000507))).matches(Hand.valueOf("xxxxxxx xxxxx - x")));
        assertFalse(ShapeBoundInf.create(ShapeSet.create(List.of(Shape._00030307, Shape._01000507))).matches(Hand.valueOf("xxxxxxx xxxx x x")));

        InfSummary summary = ShapeBoundInf.create(ShapeSet.create(List.of(Shape._00030307, Shape._01000507))).getSummary();
        assertEquals(summary, new InfSummary(ShapeSet.create(List.of(Shape._00030307, Shape._01000507)), Range.all(40), StopperSet.ALL, StopperSet.ALL));
    }
}
