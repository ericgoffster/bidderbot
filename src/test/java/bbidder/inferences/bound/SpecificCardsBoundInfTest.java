package bbidder.inferences.bound;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.Test;

import bbidder.Hand;
import bbidder.NOfTop;
import bbidder.Range;

public class SpecificCardsBoundInfTest {
    @Test
    public void test() {
        assertTrue(SpecificCardsBoundInf.create(new NOfTop(Range.exactly(2, 3), 3, 3)).matches(Hand.valueOf("AKxx Kxx xxx xxx")));
        assertFalse(SpecificCardsBoundInf.create(new NOfTop(Range.exactly(1, 3), 3, 3)).matches(Hand.valueOf("AKxx Kxx xxx xxx")));
        assertFalse(SpecificCardsBoundInf.create(new NOfTop(Range.exactly(2, 3), 3, 2)).matches(Hand.valueOf("AKxx Kxx xxx xxx")));
    }
}
