package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import bbidder.BidPatternList.Context;

public class BidPatternListTest {
    @Test
    public void testValueOf() {
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S)), BidPatternList.valueOf("1S").getBids());
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S), BidPattern.createSimpleBid(Bid._1N)),
                BidPatternList.valueOf("1S 1N").getBids());
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S), BidPattern.createSimpleBid(Bid.X).withIsOpposition(true),
                BidPattern.createSimpleBid(Bid._1N)), BidPatternList.valueOf("1S (X) 1N").getBids());
    }

    @Test
    public void testToString() {
        assertEquals("1S", BidPatternList.valueOf("1S").toString());
        assertEquals("1S 1N", BidPatternList.valueOf("1S 1N").toString());
        assertEquals("1S (X) 1N", BidPatternList.valueOf("1S (X) 1N").toString());
    }

    @Test
    public void testAddInitialPassed1() {
        BidPatternList bpl = BidPatternList.valueOf("1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(4, initpass.size());
        assertEquals("1S", initpass.get(0).toString());
        assertEquals("(P) 1S", initpass.get(1).toString());
        assertEquals("P (P) 1S", initpass.get(2).toString());
        assertEquals("(P) P (P) 1S", initpass.get(3).toString());
    }

    @Test
    public void testAddInitialPassed2() {
        BidPatternList bpl = BidPatternList.valueOf("(P) 1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(3, initpass.size());
        assertEquals("(P) 1S", initpass.get(0).toString());
        assertEquals("P (P) 1S", initpass.get(1).toString());
        assertEquals("(P) P (P) 1S", initpass.get(2).toString());
    }

    @Test
    public void testAddInitialPassed3() {
        BidPatternList bpl = BidPatternList.valueOf("P (P) 1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(2, initpass.size());
        assertEquals("P (P) 1S", initpass.get(0).toString());
        assertEquals("(P) P (P) 1S", initpass.get(1).toString());
    }

    @Test
    public void testAddInitialPassed4() {
        BidPatternList bpl = BidPatternList.valueOf("(P) P (P) 1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(1, initpass.size());
        assertEquals("(P) P (P) 1S", initpass.get(0).toString());
    }

    @Test
    public void testAddInitialPassed5() {
        BidPatternList bpl = BidPatternList.valueOf("(P) P (P)");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(2, initpass.size());
        assertEquals("(P) P (P)", initpass.get(0).toString());
        assertEquals("P (P) P (P)", initpass.get(1).toString());
    }

    @Test
    public void testAddOpposition() {
        assertEquals("1S (P) 1N", BidPatternList.valueOf("1S 1N").withOpposingBidding().toString());
        assertEquals("1C (P) 1S (P) 1N", BidPatternList.valueOf("1C 1S 1N").withOpposingBidding().toString());
        assertEquals("(P) 1N", BidPatternList.valueOf("(P) 1N").withOpposingBidding().toString());
        assertEquals("[true] 1N", BidPatternList.valueOf("[true] 1N").withOpposingBidding().toString());
        assertEquals("1C [true] 1N", BidPatternList.valueOf("1C [true] 1N").withOpposingBidding().toString());
        assertEquals("1C (P) 1S [true] 1N", BidPatternList.valueOf("1C 1S [true] 1N").withOpposingBidding().toString());
        assertEquals("1C (1S) [true] 1N", BidPatternList.valueOf("1C (1S) [true] 1N").withOpposingBidding().toString());
    }

    @Test
    public void test1() {
        assertEquals(BidPatternList.valueOf("1S 1N"),
                BidPatternList.EMPTY.withBidAdded(BidPattern.createSimpleBid(Bid._1S)).withBidAdded(BidPattern.createSimpleBid(Bid._1N)));
    }

    @Test
    public void test2() {
        BidPatternList bpl = BidPatternList.valueOf("1C");
        List<Context> l = bpl.resolveSuits(SuitTable.EMPTY).collect(Collectors.toList());
        assertEquals(4, l.size());
    }
}
