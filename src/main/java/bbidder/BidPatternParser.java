package bbidder;

import java.io.IOException;

public class BidPatternParser implements Parser<BidPattern> {
    public final InferenceRegistry reg;
    
    public BidPatternParser(InferenceRegistry reg) {
        super();
        this.reg = reg;
    }
    private String parseSuit(Input inp) throws IOException {
        return inp.readToken(ch -> ch != ')');
    }
    private BidPattern parseInterior(Input inp) throws IOException {
        inp.advanceWhite();
        if (inp.readKeyword(BidPattern.STR_NONJUMP)) {
            return BidPattern.createJump(parseSuit(inp), 0);
        } else if (inp.readKeyword(BidPattern.STR_DOUBLEJUMP)) {
            return BidPattern.createJump(parseSuit(inp), 2);
        } else if (inp.readKeyword(BidPattern.STR_JUMP)) {
            return BidPattern.createJump(parseSuit(inp), 1);
        } else if (inp.readKeyword(BidPattern.STR_REVERSE)) {
            return BidPattern.createReverse(parseSuit(inp));
        } else if (inp.readKeyword(BidPattern.STR_NONREVERSE)) {
            return BidPattern.createNonReverse(parseSuit(inp));
        } else {
            String str = inp.readToken(ch -> ch != ')');
            if (str.length() == 0) {
                return null;
            }
            Bid simpleBid = Bid.fromStr(str);
            if (simpleBid != null) {
                return BidPattern.createSimpleBid(simpleBid);
            } else {
                int level;
                try {
                    level = Integer.parseInt(str.substring(0, 1)) - 1;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid bid: '" + str + "'");
                }
                if (level < 0 || level > 6) {
                    throw new IllegalArgumentException("Invalid bid: '" + str + "'");
                }
                if (!BiddingContext.isValidSuit(str.substring(1))) {
                    throw new IllegalArgumentException("Invalid bid: " + str);
                }
                return BidPattern.createBid(level, str.substring(1));
            }
        }
    }

    @Override
    public BidPattern parse(Input inp) throws IOException {
        inp.advanceWhite();
        if (inp.ch == '[') {
            StringBuilder sb = new StringBuilder();
            inp.advance();
            inp.advanceWhite();
            while(inp.ch != ']') {
                sb.append((char)inp.ch);
            }
            if (inp.ch == ']') {
                inp.advance();
            }
            return BidPattern.createWild(reg.parseGenerality(sb.toString()));
        }
        if (inp.ch == '(') {
            inp.advance();
            BidPattern patt = parseInterior(inp);
            inp.advanceWhite();
            if (inp.ch != ')') {
                throw new IllegalArgumentException();
            }
            inp.advance();
            return patt.withIsOpposition(true);
        }
            
        return parseInterior(inp);
    }        
}