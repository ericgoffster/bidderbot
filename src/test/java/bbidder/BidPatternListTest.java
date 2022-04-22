package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

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
        assertEquals(BidPatternList.valueOf(reg, "1S 1N").withLastBidReplaced(BidPattern.createSimpleBid(Bid._2C)),
                BidPatternList.valueOf(reg, "1S 2C"));
    }

    @Test
    public void test2() {
        InferenceRegistry reg = new SimpleInferenceRegistryFactory().get();
        BidPatternList bpl = BidPatternList.valueOf(reg, "1C");
        List<BidPatternListContext> l = bpl.resolveFirstSymbol(SymbolTable.EMPTY);
        assertEquals(4, l.size());
    }
}
