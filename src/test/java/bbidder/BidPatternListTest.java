package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import bbidder.BidPatternList.Context;
import bbidder.parsers.BidPatternListParser;

public class BidPatternListTest {
    @Test
    public void testValueOf() {
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S)), BidPatternListParser.parse("1S").getBids());
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S), BidPattern.createSimpleBid(Bid._1N)), BidPatternListParser.parse("1S 1N").getBids());
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S), BidPattern.createSimpleBid(Bid.X).withIsOpposition(true),
                BidPattern.createSimpleBid(Bid._1N)), BidPatternListParser.parse("1S (X) 1N").getBids());
    }

    @Test
    public void testToString() {
        assertEquals("1S", BidPatternListParser.parse("1S").toString());
        assertEquals("1S 1N", BidPatternListParser.parse("1S 1N").toString());
        assertEquals("1S (X) 1N", BidPatternListParser.parse("1S (X) 1N").toString());
    }

    @Test
    public void testAddInitialPassed1() {
        BidPatternList bpl = BidPatternListParser.parse("1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(4, initpass.size());
        assertEquals("1S", initpass.get(0).toString());
        assertEquals("(P) 1S", initpass.get(1).toString());
        assertEquals("P (P) 1S", initpass.get(2).toString());
        assertEquals("(P) P (P) 1S", initpass.get(3).toString());
    }

    @Test
    public void testAddInitialPassed2() {
        BidPatternList bpl = BidPatternListParser.parse("(P) 1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(3, initpass.size());
        assertEquals("(P) 1S", initpass.get(0).toString());
        assertEquals("P (P) 1S", initpass.get(1).toString());
        assertEquals("(P) P (P) 1S", initpass.get(2).toString());
    }

    @Test
    public void testAddInitialPassed3() {
        BidPatternList bpl = BidPatternListParser.parse("P (P) 1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(2, initpass.size());
        assertEquals("P (P) 1S", initpass.get(0).toString());
        assertEquals("(P) P (P) 1S", initpass.get(1).toString());
    }

    @Test
    public void testAddInitialPassed4() {
        BidPatternList bpl = BidPatternListParser.parse("(P) P (P) 1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(1, initpass.size());
        assertEquals("(P) P (P) 1S", initpass.get(0).toString());
    }

    @Test
    public void testAddInitialPassed5() {
        BidPatternList bpl = BidPatternListParser.parse("(P) P (P)");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(2, initpass.size());
        assertEquals("(P) P (P)", initpass.get(0).toString());
        assertEquals("P (P) P (P)", initpass.get(1).toString());
    }

    @Test
    public void testAddOpposition() {
        assertEquals("1S (P) 1N", BidPatternListParser.parse("1S 1N").withOpposingBidding().toString());
        assertEquals("1C (P) 1S (P) 1N", BidPatternListParser.parse("1C 1S 1N").withOpposingBidding().toString());
        assertEquals("(P) 1N", BidPatternListParser.parse("(P) 1N").withOpposingBidding().toString());
        assertEquals("[true] 1N", BidPatternListParser.parse("[true] 1N").withOpposingBidding().toString());
        assertEquals("1C [true] 1N", BidPatternListParser.parse("1C [true] 1N").withOpposingBidding().toString());
        assertEquals("1C (P) 1S [true] 1N", BidPatternListParser.parse("1C 1S [true] 1N").withOpposingBidding().toString());
        assertEquals("1C (1S) [true] 1N", BidPatternListParser.parse("1C (1S) [true] 1N").withOpposingBidding().toString());
    }

    @Test
    public void test1() {
        assertEquals(BidPatternListParser.parse("1S 1N"),
                BidPatternList.EMPTY.withBidAdded(BidPattern.createSimpleBid(Bid._1S)).withBidAdded(BidPattern.createSimpleBid(Bid._1N)));
    }

    @Test
    public void test2() {
        BidPatternList bpl = BidPatternListParser.parse("1C");
        List<Context> l = new ArrayList<>();
        bpl.resolveSuits(SuitTable.EMPTY).forEach(l::add);
        assertEquals(4, l.size());
    }
}
