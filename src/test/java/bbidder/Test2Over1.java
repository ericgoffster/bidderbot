package bbidder;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class Test2Over1 {
    public static BiddingSystem bs;

    @Test
    public static void setup() throws IOException {
        BiddingSystem bs = BiddingSystem.load("", "classpath:2over1.bidding", ex -> {
            ex.printStackTrace();
        });
        bs.runTests(failure -> {
            fail(failure.toString());
        });
    }
}
