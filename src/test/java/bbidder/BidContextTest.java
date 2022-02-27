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
        return new BidPatternList(l, true);
    }

    @Test
    public void testOpposition() {
        assertTrue(new BidContext(getBids("P"),getPatterns("P")).matches());
        assertTrue(new BidContext(getBids("P", "P"), getPatterns("P")).matches());
        assertTrue(new BidContext(getBids("P", "P"), getPatterns("(P)", "P")).matches());
        assertTrue(new BidContext(getBids("P", "P", "P"), getPatterns("P", "(P)", "P")).matches());
        assertFalse(new BidContext(getBids("P", "P"), getPatterns("P", "(P)", "P")).matches());
        assertTrue(new BidContext(getBids("P", "P", "P", "P"), getPatterns("P", "P")).matches());
    }

    @Test
    public void testPasses() {
        assertTrue(new BidContext(getBids("P"), getPatterns("P")).matches());
        assertFalse(new BidContext(getBids("X"), getPatterns("P")).matches());
        assertFalse(new BidContext(getBids("XX"),getPatterns("P")).matches());
        assertFalse(new BidContext(getBids("1C"),getPatterns("P")).matches());
    }

    @Test
    public void testDouble() {
        assertTrue(new BidContext(getBids("X"), getPatterns("X")).matches());
        assertFalse(new BidContext(getBids("P"), getPatterns("X")).matches());
        assertFalse(new BidContext(getBids("XX"),getPatterns("X")).matches());
        assertFalse(new BidContext(getBids("1C"),getPatterns("X")).matches());
    }

    @Test
    public void testReDouble() {
        assertTrue(new BidContext(getBids("XX"), getPatterns("XX")).matches());
        assertFalse(new BidContext(getBids("X"), getPatterns("XX")).matches());
        assertFalse(new BidContext(getBids("1C"),getPatterns("XX")).matches());
        assertFalse(new BidContext(getBids("P"), getPatterns("XX")).matches());
    }

    @Test
    public void testSuit() {
        assertFalse(new BidContext(getBids("P"), getPatterns("1C")).matches());
        assertFalse(new BidContext(getBids("XX"),getPatterns("1C")).matches());
        assertFalse(new BidContext(getBids("X"), getPatterns("1C")).matches());
        assertTrue(new BidContext(getBids("1C"),getPatterns("1C")).matches());
        assertFalse(new BidContext(getBids("1C"),getPatterns("1D")).matches());
    }

    @Test
    public void testMinor() {
        assertFalse(new BidContext(getBids("P"),getPatterns("1m")).matches());
        assertFalse(new BidContext(getBids("X"),getPatterns("1m")).matches());
        assertFalse(new BidContext(getBids("XX"),getPatterns("1m")).matches());
        assertTrue(new BidContext(getBids("1C"),getPatterns("1m")).matches());
        assertTrue(new BidContext(getBids("1D"),getPatterns("1m")).matches());
        assertFalse(new BidContext(getBids("1H"),getPatterns("1m")).matches());
        assertFalse(new BidContext(getBids("1S"),getPatterns("1m")).matches());
        assertFalse(new BidContext(getBids("1N"),getPatterns("1m")).matches());
    }

    @Test
    public void testMajor() {
        assertFalse(new BidContext(getBids("P"),getPatterns("1M")).matches());
        assertFalse(new BidContext(getBids("X"),getPatterns("1M")).matches());
        assertFalse(new BidContext(getBids("XX"),getPatterns("1M")).matches());
        assertFalse(new BidContext(getBids("1C"),getPatterns("1M")).matches());
        assertFalse(new BidContext(getBids("1D"),getPatterns("1M")).matches());
        assertTrue(new BidContext(getBids("1H"),getPatterns("1M")).matches());
        assertTrue(new BidContext(getBids("1S"),getPatterns("1M")).matches());
        assertFalse(new BidContext(getBids("1N"),getPatterns("1M")).matches());
    }

    @Test
    public void testAny() {
        assertFalse(new BidContext(getBids("P"),getPatterns("1x")).matches());
        assertFalse(new BidContext(getBids("X"),getPatterns("1x")).matches());
        assertFalse(new BidContext(getBids("XX"),getPatterns("1x")).matches());
        assertTrue(new BidContext(getBids("1C"),getPatterns("1x")).matches());
        assertTrue(new BidContext(getBids("1D"),getPatterns("1x")).matches());
        assertTrue(new BidContext(getBids("1H"),getPatterns("1x")).matches());
        assertTrue(new BidContext(getBids("1S"),getPatterns("1x")).matches());
        assertFalse(new BidContext(getBids("1N"),getPatterns("1x")).matches());
    }

    @Test
    public void tedtSubst() {
        BidContext bc = new BidContext(getBids("1C"),getPatterns("1x"));
        assertTrue(bc.matches());
        assertEquals(bc.suits.get("x").intValue(), 0);
    }

    @Test
    public void testRaise() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "2C"),getPatterns("1x", "2x"));
        assertTrue(bc.matches());
    }

    @Test
    public void testNotRaise() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "2D"),getPatterns("1x", "2x"));
        assertFalse(bc.matches());
    }

    @Test
    public void testNotRaise2() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "2C"),getPatterns("1x", "2y"));
        assertFalse(bc.matches());
    }

    @Test
    public void testNotRaise3() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "2D"),getPatterns("1x", "2y"));
        assertTrue(bc.matches());
    }

    @Test
    public void testNotJump() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "1D"),getPatterns("1x", "NJy"));
        assertTrue(bc.matches());
    }

    @Test
    public void testNotJump2() {
        BidContext bc = new BidContext(getUnopposedBids("1H", "2C"),getPatterns("1x", "NJy"));
        assertTrue(bc.matches());
    }

    @Test
    public void testNotJump3() {
        BidContext bc = new BidContext(getUnopposedBids("1H", "2S"),getPatterns("1x", "NJy"));
        assertFalse(bc.matches());
    }

    @Test
    public void testJump() {
        BidContext bc = new BidContext(getUnopposedBids("1C", "2D"),getPatterns("1x", "Jy"));
        assertTrue(bc.matches());
    }

    @Test
    public void testJump2() {
        BidContext bc = new BidContext(getUnopposedBids("1H", "3C"),getPatterns("1x", "Jy"));
        assertTrue(bc.matches());
    }

    @Test
    public void testJump3() {
        BidContext bc = new BidContext(getUnopposedBids("1H", "3S"),getPatterns("1x", "Jy"));
        assertFalse(bc.matches());
    }
}
