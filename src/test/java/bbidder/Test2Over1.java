package bbidder;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class Test2Over1 {
    public static BiddingSystem bs;
    @BeforeAll
    public static void setup() throws IOException {
        bs = BiddingSystem.load(Test2Over1.class.getResourceAsStream("/2over1.bidding"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "AQ2 234 23 AQJ23:1C",
            "AQ2 234 AQJ23 23:1D",
            "AQ2 AQJ23 234 23:1H",
            "AQJ23 AQ2 234 23:1S",
            "AKQ KQJ 234 2345:1N",
            "AKQ AKQ AKQ 2345:2C",
            "2 234 KQJ234 234:2D",
            "2 KQJ234 234 234:2H",
            "KQJ234 2 234 234:2S",
            "AKQ AKQ Q234 234:2N",
            "2 23 234 KQJ2345:3C",
            "2 23 KQJ2345 234:3D",
            "2 KQJ2345 23 234:3H",
            "KQJ2345 2 23 234:3S",
            "2 23 23 KQJ23456:4C",
            "2 23 KQJ23456 23:4D",
            "2 KQJ23456 23 23:4H",
            "KQJ23456 2 23 23:4S",
            "2 23 2 KQJ234567:5C",
            "2 23 KQJ234567 2:5D",
            "2 KQJ234567 23 2:5H",
            "KQJ234567 2 23 2:5S",
            })
    public void openingBids(String ln) throws IOException {
        testBid(ln);
    }
    

    @ParameterizedTest
    @ValueSource(strings = {
            "234 KQ23 A23 234:1C P 1H",
            "KQ23 KQ23 23 234:1C P 1H",
            "KQ23 234 A23 234:1C P 1S",
            "KQ23 KQ234 23 23:1C P 1H",
            "KQ234 KQ234 23 2:1C P 1S",
            "23 234 KQ23 A234:1D P 1N",
            "23 234 KQ23 AQ34:1D P 2N",
            "23 23 KQ23 AQ345:1D P 2D",
            })
    public void responses1m(String ln) throws IOException {
        testBid(ln);
    }

    private void testBid(String ln) {
        String[] parts = ln.trim().split(":");
        Hand hand = Hand.valueOf(parts[0].trim());
        BidList bids = BidList.valueOf(parts[1].trim());
        BiddingState state = new BiddingState(bs);
        for(int i = 0; i < bids.bids.size() - 1; i++) {
            state = state.addBid(bids.bids.get(i));
        }
        Bid expected = bids.bids.get(bids.bids.size() - 1);
        Bid found = state.getBid(hand);
        assertEquals(expected, found);
    }
}
