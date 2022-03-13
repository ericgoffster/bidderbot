package bbidder.inferences;

import org.junit.jupiter.api.Test;

import bbidder.Inference;

public class SpecificCardsTest {
    @Test
    public void testValueOf() {
        Inference inf = SpecificCards.valueOf("1 of top 3 in S");
        System.out.println(inf);
    }
}
