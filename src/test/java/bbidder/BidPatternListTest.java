package bbidder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BidPatternListTest {
    @Test
    public void testValueOf() {
        assertEquals(List.of(BidPattern.valueOf("1S")), BidPatternList.valueOf("1S").getBids());
        assertEquals(List.of(BidPattern.valueOf("1S"), BidPattern.valueOf("1N")), BidPatternList.valueOf("1S 1N").getBids());
        assertEquals(List.of(BidPattern.valueOf("1S"), BidPattern.valueOf("(X)"), BidPattern.valueOf("1N")),
                BidPatternList.valueOf("1S (X) 1N").getBids());
    }

    @Test
    public void testToString() {
        assertEquals("1S", BidPatternList.valueOf("1S").toString());
        assertEquals("1S 1N", BidPatternList.valueOf("1S 1N").toString());
        assertEquals("1S (X) 1N", BidPatternList.valueOf("1S (X) 1N").toString());
    }

    List<BiddingContext> add2(BiddingContext... l) {
        List<BiddingContext> newL = new ArrayList<>();
        for (BiddingContext bc : l) {
            newL.add(bc.withBidPrepended(Bid.P).withBidPrepended(Bid.P));
        }
        for (BiddingContext bc : l) {
            newL.add(bc.withBidPrepended(Bid.P));
        }
        for (BiddingContext bc : l) {
            newL.add(bc);
        }
        return newL;
    }

    List<BiddingContext> add3(BiddingContext... l) {
        List<BiddingContext> newL = new ArrayList<>();
        for (BiddingContext bc : l) {
            newL.add(bc.withBidPrepended(Bid.P).withBidPrepended(Bid.P).withBidPrepended(Bid.P));
        }
        for (BiddingContext bc : l) {
            newL.add(bc.withBidPrepended(Bid.P).withBidPrepended(Bid.P));
        }
        for (BiddingContext bc : l) {
            newL.add(bc.withBidPrepended(Bid.P));
        }
        for (BiddingContext bc : l) {
            newL.add(bc);
        }
        return newL;
    }

}
