package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import bbidder.BidPatternList.Context;

public class BidPatternListTest {
    @Test
    public void testValueOf() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S)), BidPatternList.valueOf(reg, "1S").getBids());
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S), BidPattern.createSimpleBid(Bid._1N)),
                BidPatternList.valueOf(reg, "1S 1N").getBids());
        assertEquals(List.of(BidPattern.createSimpleBid(Bid._1S), BidPattern.createSimpleBid(Bid.X).withIsOpposition(true),
                BidPattern.createSimpleBid(Bid._1N)), BidPatternList.valueOf(reg, "1S (X) 1N").getBids());
    }

    @Test
    public void testToString() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals("1S", BidPatternList.valueOf(reg, "1S").toString());
        assertEquals("1S 1N", BidPatternList.valueOf(reg, "1S 1N").toString());
        assertEquals("1S (X) 1N", BidPatternList.valueOf(reg, "1S (X) 1N").toString());
    }
    
    @Test
    public void testAddInitialPassed1() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        BidPatternList bpl = BidPatternList.valueOf(reg, "1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(4, initpass.size());
        assertEquals("1S", initpass.get(0).toString());
        assertEquals("(P) 1S", initpass.get(1).toString());
        assertEquals("P (P) 1S", initpass.get(2).toString());
        assertEquals("(P) P (P) 1S", initpass.get(3).toString());
    }
    @Test
    public void testAddInitialPassed2() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        BidPatternList bpl = BidPatternList.valueOf(reg, "(P) 1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(3, initpass.size());
        assertEquals("(P) 1S", initpass.get(0).toString());
        assertEquals("P (P) 1S", initpass.get(1).toString());
        assertEquals("(P) P (P) 1S", initpass.get(2).toString());
    }
    @Test
    public void testAddInitialPassed3() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        BidPatternList bpl = BidPatternList.valueOf(reg, "P (P) 1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(2, initpass.size());
        assertEquals("P (P) 1S", initpass.get(0).toString());
        assertEquals("(P) P (P) 1S", initpass.get(1).toString());
    }
    @Test
    public void testAddInitialPassed4() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        BidPatternList bpl = BidPatternList.valueOf(reg, "(P) P (P) 1S");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(1, initpass.size());
        assertEquals("(P) P (P) 1S", initpass.get(0).toString());
    }
    @Test
    public void testAddInitialPassed5() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        BidPatternList bpl = BidPatternList.valueOf(reg, "(P) P (P)");
        List<BidPatternList> initpass = bpl.addInitialPasses();
        assertEquals(2, initpass.size());
        assertEquals("(P) P (P)", initpass.get(0).toString());
        assertEquals("P (P) P (P)", initpass.get(1).toString());
    }
    
    @Test
    public void testAddOpposition() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals("1S (P) 1N", BidPatternList.valueOf(reg, "1S 1N").withOpposingBidding().toString());
        assertEquals("1C (P) 1S (P) 1N", BidPatternList.valueOf(reg, "1C 1S 1N").withOpposingBidding().toString());
        assertEquals("(P) 1N", BidPatternList.valueOf(reg, "(P) 1N").withOpposingBidding().toString());
        assertEquals("[true] 1N", BidPatternList.valueOf(reg, "[true] 1N").withOpposingBidding().toString());
        assertEquals("1C [true] 1N", BidPatternList.valueOf(reg, "1C [true] 1N").withOpposingBidding().toString());
        assertEquals("1C (P) 1S [true] 1N", BidPatternList.valueOf(reg, "1C 1S [true] 1N").withOpposingBidding().toString());
        assertEquals("1C (1S) [true] 1N", BidPatternList.valueOf(reg, "1C (1S) [true] 1N").withOpposingBidding().toString());
    }

    @Test
    public void test1() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        assertEquals(BidPatternList.valueOf(reg, "1S 1N"),
                BidPatternList.EMPTY.withBidAdded(BidPattern.createSimpleBid(Bid._1S)).withBidAdded(BidPattern.createSimpleBid(Bid._1N)));
    }

    @Test
    public void test2() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        BidPatternList bpl = BidPatternList.valueOf(reg, "1C");
        List<Context> l = bpl.resolveSymbols(SymbolTable.EMPTY);
        assertEquals(4, l.size());
    }
}
