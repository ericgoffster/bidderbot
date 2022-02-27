package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class BidPatternListTest {
    @Test
    public void testValueOf() {
        assertEquals(new BidPatternList(List.of(new BidPattern(false, "1S")), true), BidPatternList.valueOf("1S"));
        assertEquals(new BidPatternList(List.of(new BidPattern(false, "1S"), new BidPattern(false, "1N")), true), BidPatternList.valueOf("1S 1N"));
        assertEquals(new BidPatternList(List.of(new BidPattern(false, "1S"), new BidPattern(true, "X"), new BidPattern(false, "1N")), true),
                BidPatternList.valueOf("1S (X) 1N"));
    }

    @Test
    public void testToString() {
        assertEquals("1S", new BidPatternList(List.of(new BidPattern(false, "1S")), true).toString());
        assertEquals("1S 1N", new BidPatternList(List.of(new BidPattern(false, "1S"), new BidPattern(false, "1N")), true).toString());
        assertEquals("1S (X) 1N",
                new BidPatternList(List.of(new BidPattern(false, "1S"), new BidPattern(true, "X"), new BidPattern(false, "1N")), true).toString());
    }
}
