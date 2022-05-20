package bbidder;

import java.io.IOException;

/**
 * Parses a Bid Pattern.
 * 
 * @author goffster
 *
 */
public final class BidPatternParser implements Parser<BidPattern> {
    private String parseSuit(Input inp) throws IOException {
        return inp.readToken(ch -> ch != ')');
    }

    private BidPattern parseInterior(Input inp) throws IOException {
        inp.advanceWhite();
        if (inp.readKeyword(BidPattern.STR_NONJUMP)) {
            String str = parseSuit(inp);
            Symbol symbol = SymbolParser.parseSymbol(str);
            if (symbol == null) {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            return BidPattern.createJump(symbol, 0);
        } else if (inp.readKeyword(BidPattern.STR_DOUBLEJUMP)) {
            String str = parseSuit(inp);
            Symbol symbol = SymbolParser.parseSymbol(str);
            if (symbol == null) {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            return BidPattern.createJump(symbol, 2);
        } else if (inp.readKeyword(BidPattern.STR_JUMP)) {
            String str = parseSuit(inp);
            Symbol symbol = SymbolParser.parseSymbol(str);
            if (symbol == null) {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            return BidPattern.createJump(symbol, 1);
        } else {
            String str = parseSuit(inp);
            if (str.length() == 0) {
                return null;
            }
            Bid simpleBid = Bid.fromStr(str);
            if (simpleBid != null) {
                return BidPattern.createSimpleBid(simpleBid);
            }
            if (str.length() < 1) {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            char ch = str.charAt(0);
            if (ch < '1' || ch > '7') {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            int level = ch - '1';
            Symbol symbol = SymbolParser.parseSymbol(str.substring(1));
            if (symbol == null) {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            return BidPattern.createBid(level, symbol);
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
            return BidPattern.createWild(GeneralityParser.parseGenerality(sb.toString()));
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