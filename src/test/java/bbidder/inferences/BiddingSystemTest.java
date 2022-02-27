package bbidder.inferences;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import bbidder.BiddingSystem;

public class BiddingSystemTest {
    @Test
    public void testValueOf() throws IOException {
        InputStream is = BiddingSystemTest.class.getResourceAsStream("/2over1.bidding");
        BiddingSystem bs = BiddingSystem.load(is);
        int foo = 0;
    }
}
