package bbidder.inferences;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bbidder.Constants;
import bbidder.Hand;
import bbidder.InferenceContext;

public class SpecificCardsTest {
    @Test
    public void testValueOf() {
        assertEquals(new SpecificCards("S", (short)(1L << Constants.ACE | 1L << Constants.QUEEN)), SpecificCards.valueOf("AQ in S"));
    }

    @Test
    public void testAffirmative() {
        InferenceContext ctx = new InferenceContext();
        assertTrue(SpecificCards.valueOf("AQ in S").bind(ctx).get(0).inf.matches(Hand.valueOf("AKQ JT9 876 5432")));
        assertFalse(SpecificCards.valueOf("AQ in H").bind(ctx).get(0).inf.matches(Hand.valueOf("AKQ JT9 876 5432")));
    }
}
