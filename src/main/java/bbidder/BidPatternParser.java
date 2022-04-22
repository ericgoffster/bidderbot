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
        Integer jumpLevel = null;
        String str;
        if (inp.readKeyword(BidPattern.STR_NONJUMP)) {
            jumpLevel = 0;
            str = parseSuit(inp);
        } else if (inp.readKeyword(BidPattern.STR_DOUBLEJUMP)) {
            jumpLevel = 2;
            str = parseSuit(inp);
        } else if (inp.readKeyword(BidPattern.STR_JUMP)) {
            jumpLevel = 1;
            str = parseSuit(inp);
        } else {
            str = parseSuit(inp);
            if (str.length() == 0) {
                return null;
            }
            Bid simpleBid = Bid.fromStr(str);
            if (simpleBid != null) {
                return BidPattern.createSimpleBid(simpleBid);
            }
        }
        {
            Integer level = null;
            if (str.length() > 0) {
                char ch = str.charAt(0);
                if (ch >= '1' && ch <= '7') {
                    level = ch - '1';
                    str = str.substring(1);
                }
            }
            Symbol sym = SymbolParser.parseSymbol(str);
            if (sym == null) {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            return BidPattern.createBid(jumpLevel, level, sym);
        }
    }

    @Override
    public BidPattern parse(Input inp) throws IOException {
        inp.advanceWhite();
        if (inp.ch == '[') {
            StringBuilder sb = new StringBuilder();
            inp.advance();
            inp.advanceWhite();
            while (inp.ch != ']') {
                sb.append((char) inp.ch);
                inp.advance();
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