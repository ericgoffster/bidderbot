package bbidder;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class BiddingSystemTest {
    @Test
    public void test() throws IOException {
        BiddingSystem bs = BiddingSystem.load(BiddingSystemTest.class.getResourceAsStream("/test1.bidding"));
        List<BoundInference> l = bs.getInference(new BidList(List.of(Bid._1H)));
        assertEquals(1, l.size());
    }
}
