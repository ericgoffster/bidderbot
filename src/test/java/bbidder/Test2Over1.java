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
            "AKQ KQJ 234 2345:1N"
            })
    public void test1(String ln) throws IOException {
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
