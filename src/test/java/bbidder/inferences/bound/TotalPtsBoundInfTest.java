package bbidder.inferences.bound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import bbidder.Hand;
import bbidder.InfSummary;
import bbidder.Range;
import bbidder.ShapeSet;
import bbidder.StopperSet;

public class TotalPtsBoundInfTest {
    @Test
    public void test() {
        assertEquals(TotalPtsBoundInf.create(InfSummary.ALL, Range.all(40)), ConstBoundInference.T);
        assertEquals(TotalPtsBoundInf.create(InfSummary.ALL, Range.none(40)), ConstBoundInference.F);
        assertTrue(TotalPtsBoundInf.create(InfSummary.ALL, Range.between(10, 11, 40)).test(Hand.valueOf("AKxx Kxx xxx xxx")));
        assertFalse(TotalPtsBoundInf.create(InfSummary.ALL, Range.between(10, 11, 40)).test(Hand.valueOf("AKxx Qxx xxx xxx")));
        assertTrue(TotalPtsBoundInf.create(InfSummary.ALL, Range.between(10, 11, 40)).test(Hand.valueOf("AKxx KJx xxx xxx")));
        InfSummary summary = TotalPtsBoundInf.create(InfSummary.ALL, Range.between(10, 11, 40)).getSummary();
        assertEquals(summary, new InfSummary(ShapeSet.ALL, Range.between(10, 11, 40), StopperSet.ALL, StopperSet.ALL));
    }
}
