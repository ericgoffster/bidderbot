package bbidder.parsers;

import java.io.IOException;

import bbidder.Bid;
import bbidder.BidPattern;
import bbidder.PointRange;
import bbidder.Position;
import bbidder.RangeOf;
import bbidder.Seats;
import bbidder.SuitLengthRange;
import bbidder.Symbol;
import bbidder.generalities.BestFitEstablished;
import bbidder.generalities.BidSuitGenerality;
import bbidder.generalities.FitEstablished;
import bbidder.generalities.MadeBid;
import bbidder.generalities.TotalPointsEstablished;
import bbidder.generalities.UnbidSuitGenerality;
import bbidder.generalities.WeAreThreeSuited;

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
                Seats seats = Seats.NONE;
                String seatsC = inp.readToken(ch -> ch != ':');
                for (int i = 0; i < seatsC.length(); i++) {
                    seats = seats.addSeat(seatsC.charAt(i) - '1');
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
                String tag = inp.readToken(ch -> Character.isJavaIdentifierPart(ch));
                switch(tag) {
                case "i":
                case "partner":
                    Position position = Position.getPosition(tag);
                    inp.advanceWhite();
                    if (inp.readKeyword("BID")) {
                        Symbol symbol = parseSymbol(inp);
                        SuitLengthRange range;
                        inp.advanceWhite();
                        if (inp.readKeyword("PROMISING")) {
                            RangeOf rng = new RangeParser().parse(inp);
                            range = SuitLengthRange.between(rng.min, rng.max);
                        } else {
                            range = SuitLengthRange.atLeast(0);
                        }
                        p = p.addGenerality(new BidSuitGenerality(symbol, position, range));
                    } else if (inp.readKeyword("MADE_BID")) {
                        inp.advanceWhite();
                        String t = inp.readToken(ch -> !Character.isWhitespace(ch) && ch != ':');
                        p = p.addGenerality(new MadeBid(position, t));
                    } else {
                        throw new IllegalArgumentException("Invalid bid pattern");
                    }
                    break;
                case UnbidSuitGenerality.NAME: {
                    Symbol symbol = parseSymbol(inp);
                    p = p.addGenerality(new UnbidSuitGenerality(symbol));
                    break;
                }
                case WeAreThreeSuited.NAME: {
                    p = p.addGenerality(WeAreThreeSuited.WE_R_3_SUITED);
                    break;
                }
                default:
                    inp.unread(tag);
                    RangeOf rng = new RangeParser().parse(inp);
                    if (rng != null) {
                        inp.advanceWhite();
                        if (inp.readKeyword(FitEstablished.NAME.toUpperCase())) {
                            Symbol symbol = parseSymbol(inp);
                            p = p.addGenerality(new FitEstablished(symbol, SuitLengthRange.between(rng.min, rng.max)));
                            break;
                        } else if (inp.readKeyword(BestFitEstablished.NAME.toUpperCase())) {
                            Symbol symbol = parseSymbol(inp);
                            p = p.addGenerality(new BestFitEstablished(symbol, SuitLengthRange.between(rng.min, rng.max)));
                            break;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                    String rest = inp.readToken(ch -> ch != ':');
                    PointRange createRange = CombinedPointsRangeParser.parseCombinedTPtsRange(rest.trim());
                    if (createRange != null) {
                        p = p.addGenerality(new TotalPointsEstablished(createRange));
                    } else {
                        throw new IllegalArgumentException();
                    }
                    break;
                }
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
        if (inp.readKeyword("*")) {
            return BidPattern.createWild();
        } else if (inp.readKeyword(BidPattern.STR_NONJUMP)) {
            Symbol symbol = parseSymbol(inp);
            return BidPattern.createJump(symbol, 0).withAntiMatch(anti);
        } else if (inp.readKeyword(BidPattern.STR_DOUBLEJUMP)) {
            Symbol symbol = parseSymbol(inp);
            return BidPattern.createJump(symbol, 2).withAntiMatch(anti);
        } else if (inp.readKeyword(BidPattern.STR_JUMP)) {
            Symbol symbol = parseSymbol(inp);
            return BidPattern.createJump(symbol, 1).withAntiMatch(anti);
        } else if (inp.readKeyword("?")) {
            Symbol symbol = parseSymbol(inp);
            return BidPattern.createBid(null, symbol).withAntiMatch(anti);
        } else {
            if (inp.ch >= '1' && inp.ch <= '7') {
                int level = inp.ch - '1';
                inp.advance();
                Symbol symbol = parseSymbol(inp);
                return BidPattern.createBid(level, symbol).withAntiMatch(anti);
            }
            if (inp.readKeyword("P")) {
                return BidPattern.createSimpleBid(Bid.P).withAntiMatch(anti);
            }
            if (inp.readKeyword("XX")) {
                return BidPattern.createSimpleBid(Bid.XX).withAntiMatch(anti);
            }
            if (inp.readKeyword("X")) {
                return BidPattern.createSimpleBid(Bid.X).withAntiMatch(anti);
            }
            throw new IllegalArgumentException("Invalid bid");
        }
    }

    private Symbol parseSymbol(Input inp) throws IOException {
        String str = parseSuit(inp);
        Symbol symbol = SymbolParser.parseSymbol(str);
        if (symbol == null) {
            throw new IllegalArgumentException("Invalid bid: " + str);
        }
        return symbol;
    }

    @Override
    public BidPattern parse(Input inp) throws IOException {
        inp.advanceWhite();
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