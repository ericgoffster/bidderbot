package bbidder.inferences.bound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.PointRange;
import bbidder.Shape;
import bbidder.ShapeSet;
import bbidder.parsers.HandParser;

public class OrBoundInfTest {
    @Test
    public void test() {
        assertEquals(OrBoundInf.create(List.of()), ConstBoundInference.F);
        assertEquals(OrBoundInf.create(ConstBoundInference.T, ConstBoundInference.F), ConstBoundInference.T);
        assertEquals(OrBoundInf.create(ConstBoundInference.T, ConstBoundInference.T), ConstBoundInference.T);
        IBoundInference i1 = ShapeBoundInf.create(ShapeSet.create(List.of(Shape._04030303)));
        IBoundInference i2 = TotalPtsBoundInf.create(InfSummary.ALL, PointRange.between(10, 11));
        IBoundInference i3 = OrBoundInf.create(i1, i2);
        assertEquals(i3.getSummary(), InfSummary.ALL);

        assertTrue(i3.test(HandParser.parseHand("AKQ xxx xxx xxxx")));
        assertTrue(i3.test(HandParser.parseHand("AKQ Jxx xxx xxxx")));
        assertTrue(i3.test(HandParser.parseHand("AKQ Jxx xxxx xxx")));
        assertFalse(i3.test(HandParser.parseHand("AKJ xxx xxxxx xx")));

    }

    @Test
    public void test1() {
        IBoundInference i1 = ShapeBoundInf.create(ShapeSet.create(List.of(Shape._04030303, Shape._03040303)));
        IBoundInference i2 = ShapeBoundInf.create(ShapeSet.create(List.of(Shape._04030303, Shape._03030403)));
        IBoundInference i3 = OrBoundInf.create(OrBoundInf.create(i1, i2), ConstBoundInference.F);
        assertEquals(i3, ShapeBoundInf.create(ShapeSet.create(List.of(Shape._04030303, Shape._03040303, Shape._03030403))));
    }
}
