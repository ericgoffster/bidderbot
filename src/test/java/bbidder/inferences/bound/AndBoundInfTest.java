package bbidder.inferences.bound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import bbidder.Hand;
import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.PointRange;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.StopperSet;

public class AndBoundInfTest {
    @Test
    public void test() {
        assertEquals(AndBoundInf.create(List.of()), ConstBoundInference.T);
        assertEquals(AndBoundInf.create(ConstBoundInference.T, ConstBoundInference.F), ConstBoundInference.F);
        assertEquals(AndBoundInf.create(ConstBoundInference.T, ConstBoundInference.T), ConstBoundInference.T);
        IBoundInference i1 = ShapeBoundInf.create(ShapeSet.create(List.of(Shape._04030303)));
        IBoundInference i2 = TotalPtsBoundInf.create(InfSummary.ALL, PointRange.between(10, 11));
        IBoundInference i3 = AndBoundInf.create(i1, i2);
        InfSummary infSummary = new InfSummary(ShapeSet.create(List.of(Shape._04030303)), PointRange.between(10, 11), PointRange.atMost(11),
                StopperSet.ALL, StopperSet.ALL);
        assertEquals(i3.getSummary(), infSummary);
        assertFalse(i3.test(Hand.valueOf("AKQ xxx xxx xxxx")));
        assertTrue(i3.test(Hand.valueOf("AKQ Jxx xxx xxxx")));
        assertFalse(i3.test(Hand.valueOf("AKQ Jxx xxxx xxx")));
    }

    @Test
    public void test1() {
        IBoundInference i1 = ShapeBoundInf.create(ShapeSet.create(List.of(Shape._04030303, Shape._03040303)));
        IBoundInference i2 = ShapeBoundInf.create(ShapeSet.create(List.of(Shape._04030303, Shape._03030403)));
        IBoundInference i3 = AndBoundInf.create(AndBoundInf.create(i1, i2), ConstBoundInference.T);
        assertEquals(i3, ShapeBoundInf.create(ShapeSet.create(List.of(Shape._04030303))));
    }
}
