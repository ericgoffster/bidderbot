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

public class HcpBoundInfTest {
    @Test
    public void test() {
        assertEquals(HcpBoundInf.create(Range.all(40)), ConstBoundInference.T);
        assertEquals(HcpBoundInf.create(Range.none(40)), ConstBoundInference.F);
        assertTrue(HcpBoundInf.create(Range.between(10, 11, 40)).matches(Hand.valueOf("AKxx Kxx xxx xxx")));
        assertFalse(HcpBoundInf.create(Range.between(10, 11, 40)).matches(Hand.valueOf("AKxx Qxx xxx xxx")));
        assertTrue(HcpBoundInf.create(Range.between(10, 11, 40)).matches(Hand.valueOf("AKxx KJx xxx xxx")));
        InfSummary summary = HcpBoundInf.create(Range.between(10, 11, 40)).getSummary();
        assertEquals(summary, new InfSummary(ShapeSet.ALL, Range.atLeast(10, 40), StopperSet.ALL));
    }
}
