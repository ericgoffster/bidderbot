package bbidder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BidContextTest {
    public static BidList getBids(String... bids) {
        List<Bid> l = new ArrayList<>();
        for (String b : bids) {
            l.add(Bid.fromStr(b));
        }
        return new BidList(l);
    }

    public static BidList getUnopposedBids(String... bids) {
        List<Bid> l = new ArrayList<>();
        for (String b : bids) {
            l.add(Bid.P);
            l.add(Bid.fromStr(b));
        }
        return new BidList(l);
    }

    public static BidPatternList getPatterns(String... bids) {
        List<BidPattern> l = new ArrayList<>();
        for (String b : bids) {
            l.add(BidPattern.valueOf(b));
        }
        return new BidPatternList(l);
    }

    @Test
    public void testOpposition() {
        assertTrue(new BidContext(getBids("P")).matches(getPatterns("P")));
        assertTrue(new BidContext(getBids("P", "P")).matches(getPatterns("P")));
        assertTrue(new BidContext(getBids("P", "P")).matches(getPatterns("(P)", "P")));
        assertTrue(new BidContext(getBids("P", "P", "P")).matches(getPatterns("P", "(P)", "P")));
        assertFalse(new BidContext(getBids("P", "P")).matches(getPatterns("P", "(P)", "P")));
        assertTrue(new BidContext(getBids("P", "P", "P", "P")).matches(getPatterns("P", "P")));
    }

    @Test
    public void testPasses() {
        assertTrue(new BidContext(getBids("P")).matches(getPatterns("P")));
        assertFalse(new BidContext(getBids("P")).matches(getPatterns("X")));
        assertFalse(new BidContext(getBids("P")).matches(getPatterns("XX")));
        assertFalse(new BidContext(getBids("P")).matches(getPatterns("1C")));
    }

    @Test
    public void testDouble() {
        assertTrue(new BidContext(getBids("X")).matches(getPatterns("X")));
        assertFalse(new BidContext(getBids("X")).matches(getPatterns("P")));
        assertFalse(new BidContext(getBids("X")).matches(getPatterns("XX")));
        assertFalse(new BidContext(getBids("X")).matches(getPatterns("1C")));
    }

    @Test
    public void testReDouble() {
        assertTrue(new BidContext(getBids("XX")).matches(getPatterns("XX")));
        assertFalse(new BidContext(getBids("XX")).matches(getPatterns("P")));
        assertFalse(new BidContext(getBids("XX")).matches(getPatterns("X")));
        assertFalse(new BidContext(getBids("XX")).matches(getPatterns("1C")));
    }

    @Test
    public void testSuit() {
        assertTrue(new BidContext(getBids("1C")).matches(getPatterns("1C")));
        assertFalse(new BidContext(getBids("1C")).matches(getPatterns("P")));
        assertFalse(new BidContext(getBids("1C")).matches(getPatterns("X")));
        assertFalse(new BidContext(getBids("1C")).matches(getPatterns("XX")));
        assertFalse(new BidContext(getBids("1C")).matches(getPatterns("1D")));
    }

    @Test
    public void testMinor() {
        assertFalse(new BidContext(getBids("P")).matches(getPatterns("1m")));
        assertFalse(new BidContext(getBids("X")).matches(getPatterns("1m")));
        assertFalse(new BidContext(getBids("XX")).matches(getPatterns("1m")));
        assertTrue(new BidContext(getBids("1C")).matches(getPatterns("1m")));
        assertTrue(new BidContext(getBids("1D")).matches(getPatterns("1m")));
        assertFalse(new BidContext(getBids("1H")).matches(getPatterns("1m")));
        assertFalse(new BidContext(getBids("1S")).matches(getPatterns("1m")));
        assertFalse(new BidContext(getBids("1N")).matches(getPatterns("1m")));
    }

    @Test
    public void testMajor() {
        assertFalse(new BidContext(getBids("P")).matches(getPatterns("1M")));
        assertFalse(new BidContext(getBids("X")).matches(getPatterns("1M")));
        assertFalse(new BidContext(getBids("XX")).matches(getPatterns("1M")));
        assertFalse(new BidContext(getBids("1C")).matches(getPatterns("1M")));
        assertFalse(new BidContext(getBids("1D")).matches(getPatterns("1M")));
        assertTrue(new BidContext(getBids("1H")).matches(getPatterns("1M")));
        assertTrue(new BidContext(getBids("1S")).matches(getPatterns("1M")));
        assertFalse(new BidContext(getBids("1N")).matches(getPatterns("1M")));
    }

    @Test
    public void testAny() {
        assertFalse(new BidContext(getBids("P")).matches(getPatterns("1x")));
        assertFalse(new BidContext(getBids("X")).matches(getPatterns("1x")));
        assertFalse(new BidContext(getBids("XX")).matches(getPatterns("1m")));
        assertTrue(new BidContext(getBids("1C")).matches(getPatterns("1x")));
        assertTrue(new BidContext(getBids("1D")).matches(getPatterns("1x")));
        assertTrue(new BidContext(getBids("1H")).matches(getPatterns("1x")));
        assertTrue(new BidContext(getBids("1S")).matches(getPatterns("1x")));
        assertFalse(new BidContext(getBids("1N")).matches(getPatterns("1x")));
    }

    @Test
    public void tedtSubst() {
        BidContext bc = new BidContext(getBids("1C"));
        assertTrue(bc.matches(getPatterns("1x")));
        assertEquals(bc.suits.get("x").intValue(), 0);
    }

    @Test
    public void testRaise() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "2C"));
        assertTrue(bc.matches(getPatterns("1x", "2x")));
    }

    @Test
    public void testNotRaise() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "2D"));
        assertFalse(bc.matches(getPatterns("1x", "2x")));
    }

    @Test
    public void testNotRaise2() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "2C"));
        assertFalse(bc.matches(getPatterns("1x", "2y")));
    }

    @Test
    public void testNotRaise3() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "2D"));
        assertTrue(bc.matches(getPatterns("1x", "2y")));
    }

    @Test
    public void testNotJump() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "1D"));
        assertTrue(bc.matches(getPatterns("1x", "NJy")));
    }

    @Test
    public void testNotJump2() {
        BidContext bc = new BidContext(getUnopposedBids("1H", "2C"));
        assertTrue(bc.matches(getPatterns("1x", "NJy")));
    }

    @Test
    public void testNotJump3() {
        BidContext bc = new BidContext(getUnopposedBids("1H", "2S"));
        assertFalse(bc.matches(getPatterns("1x", "NJy")));
    }

    @Test
    public void testJump() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "2D"));
        assertTrue(bc.matches(getPatterns("1x", "Jy")));
    }

    @Test
    public void testJump2() {
        BidContext bc = new BidContext(getUnopposedBids("1H", "3C"));
        assertTrue(bc.matches(getPatterns("1x", "Jy")));
    }

    @Test
    public void testJump3() {
        BidContext bc = new BidContext(getUnopposedBids("1H", "3S"));
        assertFalse(bc.matches(getPatterns("1x", "Jy")));
    }
}
