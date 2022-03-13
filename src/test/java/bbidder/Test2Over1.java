package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class Test2Over1 {
    public static BiddingSystem bs;

    @Test
    public void test() throws IOException {
        BiddingSystem bs = BiddingSystem.load("", "classpath:2over1.bidding", ex -> {
            ex.printStackTrace();
        });
        for(BiddingTest test: bs.tests) {
            TestResult result = test.getResult(bs);
            assertEquals(result.hand + ":" + result.bids, result.expected, result.found);
        }
        
        assertTrue(bs.tests.size() > 0);
    }
}
