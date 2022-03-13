package bbidder.inferences.bound;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.Test;

import bbidder.Hand;

public class SpecificCardsBoundInfTest {
    @Test
    public void test() {
        assertTrue(SpecificCardsBoundInf.create(Hand.valueOf("AK - - -")).matches(Hand.valueOf("AKxx Kxx xxx xxx")));
        assertTrue(SpecificCardsBoundInf.create(Hand.valueOf("A - - -")).matches(Hand.valueOf("AKxx Kxx xxx xxx")));
        assertFalse(SpecificCardsBoundInf.create(Hand.valueOf("AKQ - - -")).matches(Hand.valueOf("AKxx Kxx xxx xxx")));
        
        assertFalse(SpecificCardsBoundInf.create(Hand.valueOf("AK - - -")).negate().matches(Hand.valueOf("AKxx Kxx xxx xxx")));
        assertTrue(SpecificCardsBoundInf.create(Hand.valueOf("AKQ - - -")).negate().matches(Hand.valueOf("AKxx Kxx xxx xxx")));
    }
}
