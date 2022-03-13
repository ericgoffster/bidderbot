package bbidder;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Test2Over1 {
    public static BiddingSystem bs;

    @Test
    public static void setup() throws IOException {
        BiddingSystem bs = BiddingSystem.load("", "classpath:2over1.bidding", ex -> {
            ex.printStackTrace();
        });
        List<TestFailure> failures = new ArrayList<>();
        bs.runTests(failure -> {
            failures.add(failure);
        });
        assertEquals(List.of(), failures);
    }
}
