package bbidder.parsers;

import java.io.IOException;

import bbidder.Bid;
import bbidder.BidPattern;
import bbidder.Symbol;

/**
 * Parses a Bid Pattern.
 * 
 * @author goffster
 *
 */
public final class BidPatternParser implements Parser<BidPattern> {
    private String parseSuit(Input inp) throws IOException {
        return inp.readToken(ch -> ch != ')' && ch != ':');
    }

    public BidPattern parseInterior2(Input inp) throws IOException {
        BidPattern p = parseInterior(inp);
        while (inp.readKeyword(":")) {
            if (inp.readKeyword("<")) {
                BidPattern other = parseInterior(inp);
                if (other == null) {
                    throw new IllegalArgumentException("Invalid less than");
                }
                p = p.withLessThan(other);
            } else if (inp.readKeyword(">")) {
                BidPattern other = parseInterior(inp);
                if (other == null) {
                    throw new IllegalArgumentException("Invalid greater than");
                }
                p = p.withGreaterThan(other);
            } else if (inp.readKeyword("DOWN")) {
                p = p.withDownTheLine(true);
            } else if (inp.readKeyword("SEATS")) {
                short seats = 0;
                String seatsC = inp.readToken(ch -> ch != ':');
                for (int i = 0; i < seatsC.length(); i++) {
                    seats |= (1 << (seatsC.charAt(i) - '1'));
                }
                p = p.withSeats(seats);
            } else if (inp.readKeyword("\"")) {
                StringBuilder tag = new StringBuilder();
                while (inp.ch >= 0 && inp.ch != '"') {
                    tag.append((char) inp.ch);
                    inp.advance();
                }
                if (inp.ch == '"') {
                    inp.advance();
                }
                p = p.withTags(p.tags.addTag(tag.toString()));
            } else {
                throw new IllegalArgumentException("bad modifier");
            }
        }
        return p;
    }

    private BidPattern parseInterior(Input inp) throws IOException {
        inp.advanceWhite();
        boolean anti = false;
        if (inp.readKeyword("~")) {
            anti = true;
        }
        if (inp.readKeyword(BidPattern.STR_NONJUMP)) {
            String str = parseSuit(inp);
            Symbol symbol = SymbolParser.parseSymbol(str);
            if (symbol == null) {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            return BidPattern.createJump(symbol, 0).withAntiMatch(anti);
        } else if (inp.readKeyword(BidPattern.STR_DOUBLEJUMP)) {
            String str = parseSuit(inp);
            Symbol symbol = SymbolParser.parseSymbol(str);
            if (symbol == null) {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            return BidPattern.createJump(symbol, 2).withAntiMatch(anti);
        } else if (inp.readKeyword(BidPattern.STR_JUMP)) {
            String str = parseSuit(inp);
            Symbol symbol = SymbolParser.parseSymbol(str);
            if (symbol == null) {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            return BidPattern.createJump(symbol, 1).withAntiMatch(anti);
        } else if (inp.readKeyword("?")) {
            String str = parseSuit(inp);
            Symbol symbol = SymbolParser.parseSymbol(str);
            if (symbol == null) {
                throw new IllegalArgumentException("Invalid bid: " + str);
            }
            return BidPattern.createBid(null, symbol).withAntiMatch(anti);
        } else {
            String str = parseSuit(inp);
            if (str.length() == 0) {
                return null;
            }
            Bid simpleBid = Bid.fromStr(str);
            if (simpleBid != null) {
                return BidPattern.createSimpleBid(simpleBid).withAntiMatch(anti);
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
            return BidPattern.createBid(level, symbol).withAntiMatch(anti);
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
            BidPattern patt = parseInterior2(inp);
            inp.advanceWhite();
            if (inp.ch != ')') {
                throw new IllegalArgumentException();
            }
            inp.advance();
            return patt.withIsOpposition(true);
        }

        return parseInterior2(inp);
    }
}