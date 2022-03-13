package bbidder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.rules.ErrorCollector;

public class Test2Over1 {
    public static BiddingSystem bs;

    @Test
    public void test() throws Throwable {
        MyClass collector = new MyClass();
        BiddingSystem bs = BiddingSystem.load("classpath:2over1.bidding", ex -> {
            collector.addError(ex);
        });
        Random r = new Random();
        for (BiddingTest test : bs.tests) {
            TestResult result = test.getResult(r, bs);
            collector.checkThat(result.where + ":" + result.hand + ":" + result.bids + " conflicted with " + result.found, result.found.bid, equalTo(result.expected));
            if (!result.found.bid.equals(result.expected)) {
                test.getResult(r, bs);
            }
        }

        assertTrue(bs.tests.size() > 0);

        collector.verify();
    }

    static class MyClass extends ErrorCollector {
        @Override
        public void verify() throws Throwable {
            super.verify();
        }
    }
}
