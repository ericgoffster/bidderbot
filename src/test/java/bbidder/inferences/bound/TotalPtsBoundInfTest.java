package bbidder.inferences.bound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import bbidder.InfSummary;
import bbidder.PointRange;
import bbidder.ShapeSet;
import bbidder.StopperSet;
import bbidder.parsers.HandParser;

public class TotalPtsBoundInfTest {
    @Test
    public void test() {
        assertEquals(TotalPtsBoundInf.create(InfSummary.ALL, PointRange.ALL), ConstBoundInference.T);
        assertEquals(TotalPtsBoundInf.create(InfSummary.ALL, PointRange.NONE), ConstBoundInference.F);
        assertTrue(TotalPtsBoundInf.create(InfSummary.ALL, PointRange.between(10, 11)).test(HandParser.parseHand("AKxx Kxx xxx xxx")));
        assertFalse(TotalPtsBoundInf.create(InfSummary.ALL, PointRange.between(10, 11)).test(HandParser.parseHand("AKxx Qxx xxx xxx")));
        assertTrue(TotalPtsBoundInf.create(InfSummary.ALL, PointRange.between(10, 11)).test(HandParser.parseHand("AKxx KJx xxx xxx")));
        InfSummary summary = TotalPtsBoundInf.create(InfSummary.ALL, PointRange.between(10, 11)).getSummary();
        assertEquals(summary, new InfSummary(ShapeSet.ALL, PointRange.between(10, 11), PointRange.atMost(11), StopperSet.ALL, StopperSet.ALL));
    }
}
