package bbidder.inferences.bound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import bbidder.Hand;
import bbidder.InfSummary;
import bbidder.PointRange;
import bbidder.ShapeSet;
import bbidder.StopperSet;

public class HcpBoundInfTest {
    @Test
    public void test() {
        assertEquals(HcpBoundInf.create(PointRange.ALL), ConstBoundInference.T);
        assertEquals(HcpBoundInf.create(PointRange.NONE), ConstBoundInference.F);
        assertTrue(HcpBoundInf.create(PointRange.between(10, 11)).test(Hand.valueOf("AKxx Kxx xxx xxx")));
        assertFalse(HcpBoundInf.create(PointRange.between(10, 11)).test(Hand.valueOf("AKxx Qxx xxx xxx")));
        assertTrue(HcpBoundInf.create(PointRange.between(10, 11)).test(Hand.valueOf("AKxx KJx xxx xxx")));
        InfSummary summary = HcpBoundInf.create(PointRange.between(10, 11)).getSummary();
        assertEquals(summary, new InfSummary(ShapeSet.ALL, PointRange.atLeast(10), PointRange.atLeast(10), StopperSet.ALL, StopperSet.ALL));
    }
}
