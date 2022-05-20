package bbidder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import bbidder.symbols.MajorSymbol;
import bbidder.symbols.OtherMajorSymbol;

public class BidPatternParserTest {
    public static BidPattern parse(String str) throws IOException {
        BidPatternParser parser = new BidPatternParser();
        BidPattern patt;
        try (Input inp = new Input(new StringReader(str))) {
            patt = parser.parse(inp);
            inp.advanceWhite();
            if (inp.ch != -1) {
                throw new IllegalArgumentException();
            }
        }
        return patt;
    }

    @Test
    public void test() throws IOException {
        assertEquals(BidPattern.createSimpleBid(Bid.P), parse("P"));
        assertEquals(BidPattern.createSimpleBid(Bid.X), parse("X"));
        assertEquals(BidPattern.createSimpleBid(Bid.XX), parse("XX"));
        assertEquals(BidPattern.createSimpleBid(Bid._1S), parse("1S"));
        assertEquals(BidPattern.createBid(0, new MajorSymbol()), parse("1M"));
        assertEquals(BidPattern.createBid(0, new OtherMajorSymbol()), parse("1OM"));
        assertEquals(BidPattern.createJump(new MajorSymbol(), 0), parse("NJM"));
        assertEquals(BidPattern.createJump(new MajorSymbol(), 1), parse("JM"));
        assertEquals(BidPattern.createJump(new MajorSymbol(), 2), parse("DJM"));
    }
}
