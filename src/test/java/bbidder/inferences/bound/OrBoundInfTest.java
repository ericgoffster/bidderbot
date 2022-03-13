package bbidder.inferences.bound;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import bbidder.IBoundInference;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.Shape;
import bbidder.ShapeSet;

public class OrBoundInfTest {
    @Test
    public void test() {
        assertEquals(OrBoundInf.create(List.of()), ConstBoundInference.F);
        assertEquals(OrBoundInf.create(ConstBoundInference.T, ConstBoundInference.F), ConstBoundInference.T);
        assertEquals(OrBoundInf.create(ConstBoundInference.T, ConstBoundInference.T), ConstBoundInference.T);
        IBoundInference i1 = ShapeBoundInf.create(new ShapeSet(List.of(Shape._04030303)));
        IBoundInference i2 = TotalPtsBoundInf.create(InfSummary.ALL, Range.between(10, 11, 40));
        IBoundInference i3 = OrBoundInf.create(i1, i2);
        assertEquals(i3.getSummary(), InfSummary.ALL);
    }
    @Test
    public void test1() {
        IBoundInference i1 = ShapeBoundInf.create(new ShapeSet(List.of(Shape._04030303, Shape._03040303)));
        IBoundInference i2 = ShapeBoundInf.create(new ShapeSet(List.of(Shape._04030303, Shape._03030403)));
        IBoundInference i3 = OrBoundInf.create(OrBoundInf.create(i1, i2), ConstBoundInference.F);
        assertEquals(i3, ShapeBoundInf.create(new ShapeSet(List.of(Shape._04030303, Shape._03040303, Shape._03030403))));
    }
}
