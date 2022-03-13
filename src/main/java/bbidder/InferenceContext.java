package bbidder;

import static bbidder.Constants.*;

import java.io.CharArrayReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * An inference in the context of other inferences.
 * 
 * @author goffster
 *
 */
public class InferenceContext {

    public final BiddingContext bc;

    public final LikelyHands likelyHands;

    public final Bid lastBidSuit;

    public InferenceContext(Bid lastBidSuit, LikelyHands likelyHands, BiddingContext bc) {
        super();
        this.lastBidSuit = lastBidSuit;
        this.likelyHands = likelyHands;
        this.bc = bc;
    }

    public InferenceContext() {
        super();
        this.lastBidSuit = null;
        this.likelyHands = new LikelyHands();
        this.bc = new BiddingContext(new BidList(List.of()), Map.of());
    }

    public int lookupSuit(String s) {
        switch (s.toUpperCase()) {
        case STR_CLUB:
            return CLUB;
        case STR_DIAMOND:
            return DIAMOND;
        case STR_HEART:
            return HEART;
        case STR_SPADE:
            return SPADE;
        default:
            Integer s2 = bc.getSuit(s);
            if (s2 == null) {
                throw new IllegalArgumentException("Unknown Suit: '" + s + "'");
            }
            return s2;
        }
    }

    /**
     * Represents the parser state.
     * 
     * @author goffster
     *
     */
    private class ReadState implements Closeable {
        int ch;
        CharArrayReader rd;

        public ReadState(String str) throws IOException {
            rd = new CharArrayReader(str.toCharArray());
            advance();
        }

        @Override
        public void close() {
            rd.close();
        }

        public short lookupSuitSet0() throws IOException {
            readWhite();
            if (ch == '(') {
                short s = lookupSuitSet();
                readWhite();
                if (ch != ')') {
                    throw new IllegalArgumentException("Expected )");
                }
                advance();
                return s;
            } else if (ch == '~') {
                advance();
                return (short) (0xf & ~lookupSuitSet0());
            } else {
                StringBuilder sb = new StringBuilder();
                while (Character.isLetter(ch) || ch == '_' || Character.isDigit(ch)) {
                    sb.append((char) ch);
                    advance();
                }
                switch (sb.toString().toUpperCase()) {
                case "UNBID": {
                    short suits = 0;
                    for (int suit = 0; suit < 4; suit++) {
                        if (likelyHands.lho.avgLenInSuit(suit) < 4 && likelyHands.rho.avgLenInSuit(suit) < 4
                                && likelyHands.partner.avgLenInSuit(suit) < 4 && likelyHands.me.avgLenInSuit(suit) < 4) {
                            suits |= (1 << suit);
                        }
                    }
                    return suits;
                }
                case "SAME_LEVEL": {
                    if (lastBidSuit == null) {
                        return 0xf;
                    }
                    short suits = 0;
                    for (int suit = lastBidSuit.strain + 1; suit < 4; suit++) {
                        suits |= (1 << suit);
                    }
                    return suits;
                }
                case "MINORS":
                    return MINORS;
                case "MAJORS":
                    return MAJORS;
                case "REDS":
                    return REDS;
                case "BLACKS":
                    return BLACKS;
                case "ROUND":
                    return ROUND;
                case "POINTED":
                    return POINTED;
                case "ALL":
                    return ALL_SUITS;
                case "NONE":
                    return 0;
                case STR_CLUB:
                    return (1 << CLUB);
                case STR_DIAMOND:
                    return (1 << DIAMOND);
                case STR_HEART:
                    return (1 << HEART);
                case STR_SPADE:
                    return (1 << SPADE);
                default:
                    throw new IllegalArgumentException("Unknown Suit Set: '" + sb + "'");
                }
            }
        }

        public short lookupSuitSet() throws IOException {
            readWhite();
            short result = lookupSuitSet0();
            for (;;) {
                readWhite();
                if (ch != '&') {
                    break;
                }
                advance();
                result &= lookupSuitSet0();
            }
            return result;
        }

        private void advance() throws IOException {
            ch = rd.read();
        }

        private void readWhite() throws IOException {
            while (ch == ' ' || ch == '\t') {
                advance();
            }
        }
    }

    /**
     * @param str The string to parse
     * @return The suits bound to the given expression
     */
    public short lookupSuitSet(String str) {
        try (ReadState state = new ReadState(str)) {
            short suits = state.lookupSuitSet();
            state.readWhite();
            if (state.ch != -1) {
                throw new IllegalArgumentException("Undecipherable characters at end");
            }
            return suits;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Suit Set: '" + str + "'", e);
        }
    }

    /**
     * @param str The string to parse
     * @return The high card point range represented by the string.
     */
    public int resolvePoints(String str) {
        if (str.matches("\\d+")) {
            return Integer.parseInt(str);
        }
        throw new IllegalArgumentException("Invalid Points: '" + str + "'");
    }

    /**
     * @param str The string to parse
     * @return The length represented by the string.
     */
    public int resolveLength(String str) {
        if (str.matches("\\d+")) {
            return Integer.parseInt(str);
        }
        throw new IllegalArgumentException("Invalid Length: '" + str + "'");
    }
}