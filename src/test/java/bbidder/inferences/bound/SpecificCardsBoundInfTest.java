package bbidder.inferences.bound;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.Test;

import bbidder.CardsRange;
import bbidder.NOfTop;
import bbidder.parsers.HandParser;

public class SpecificCardsBoundInfTest {
    @Test
    public void test() {
        assertTrue(SpecificCardsBoundInf.create(new NOfTop(CardsRange.exactly(2), 3, 3)).test(HandParser.valueOf("AKxx Kxx xxx xxx")));
        assertFalse(SpecificCardsBoundInf.create(new NOfTop(CardsRange.exactly(1), 3, 3)).test(HandParser.valueOf("AKxx Kxx xxx xxx")));
        assertFalse(SpecificCardsBoundInf.create(new NOfTop(CardsRange.exactly(2), 3, 2)).test(HandParser.valueOf("AKxx Kxx xxx xxx")));
    }
}
