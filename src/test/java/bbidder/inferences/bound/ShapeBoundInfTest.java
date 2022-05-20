package bbidder.inferences.bound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import bbidder.InfSummary;
import bbidder.PointRange;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.StopperSet;
import bbidder.parsers.HandParser;

public class ShapeBoundInfTest {
    @Test
    public void test() {
        assertEquals(ShapeBoundInf.create(ShapeSet.ALL), ConstBoundInference.T);
        assertEquals(ShapeBoundInf.create(ShapeSet.NONE), ConstBoundInference.F);
        assertTrue(ShapeBoundInf.create(ShapeSet.create(List.of(Shape._00030307, Shape._01000507))).test(HandParser.valueOf("xxxxxxx xxxxx - x")));
        assertFalse(ShapeBoundInf.create(ShapeSet.create(List.of(Shape._00030307, Shape._01000507))).test(HandParser.valueOf("xxxxxxx xxxx x x")));

        InfSummary summary = ShapeBoundInf.create(ShapeSet.create(List.of(Shape._00030307, Shape._01000507))).getSummary();
        assertEquals(summary, new InfSummary(ShapeSet.create(List.of(Shape._00030307, Shape._01000507)), PointRange.ALL, PointRange.ALL,
                StopperSet.ALL, StopperSet.ALL));
    }
}
