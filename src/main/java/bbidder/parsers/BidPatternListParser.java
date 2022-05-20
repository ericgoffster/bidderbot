package bbidder.parsers;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import bbidder.BidPattern;
import bbidder.BidPatternList;

public class BidPatternListParser {

    /**
     * @param str
     *            The String to parse
     * @return A parsed BidPatternList
     */
    public static BidPatternList parse(String str) {
        if (str == null) {
            return null;
        }
        ListParser<BidPattern> parser = new ListParser<BidPattern>(new BidPatternParser(), "");
        try (Input inp = new Input(new StringReader(str))) {
            List<BidPattern> l = parser.parse(inp);
            inp.advanceWhite();
            if (inp.ch != -1) {
                throw new IllegalArgumentException("invalid bids: '" + str + "'");
            }
            int numWild = 0;
            for (BidPattern patt : l) {
                if (patt.isWild) {
                    numWild++;
                }
            }
            if (numWild > 1) {
                throw new IllegalArgumentException("Only one wildcard allowed");
            }
            return BidPatternList.create(l);
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid bids: '" + str + "'", e);
        }
    }

}
