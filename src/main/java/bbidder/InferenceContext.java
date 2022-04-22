package bbidder;

import static bbidder.Constants.ALL_SUITS;
import static bbidder.Constants.BLACKS;
import static bbidder.Constants.MAJORS;
import static bbidder.Constants.MINORS;
import static bbidder.Constants.POINTED;
import static bbidder.Constants.REDS;
import static bbidder.Constants.ROUND;

import java.io.CharArrayReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An inference in the context of other inferences.
 * 
 * @author goffster
 *
 */
public class InferenceContext {

    public final BiddingContext bc;

    public final Players players;

    public InferenceContext(Players players, BiddingContext bc) {
        super();
        this.players = players;
        this.bc = bc;
    }

    public InferenceContext() {
        super();
        this.players = new Players();
        this.bc = BiddingContext.EMPTY;
    }

    public Map<Integer, InferenceContext> lookupSuits(String s) {
        Integer strain = Strain.getSuit(s);
        if (strain != null) {
            return Map.of(strain, this);
        }
        Map<Integer, InferenceContext> result = new LinkedHashMap<>();
        for (var e : bc.getMappedBiddingContexts(s).entrySet()) {
            result.put(e.getKey(), new InferenceContext(players, e.getValue()));
        }
        return result;
    }
    
    public interface SuitSet {
        public short evaluate();
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

        public SuitSet lookupSuitSet0() throws IOException {
            readWhite();
            if (ch == '(') {
                SuitSet s = lookupSuitSet();
                readWhite();
                if (ch != ')') {
                    throw new IllegalArgumentException("Expected )");
                }
                advance();
                return s;
            } else if (ch == '~') {
                advance();
                SuitSet ss = lookupSuitSet0();
                return () -> (short) (0xf & ~ss.evaluate());
            } else if (ch == '>') {
                advance();
                StringBuilder sb = new StringBuilder();
                while (Character.isLetter(ch) || ch == '_' || Character.isDigit(ch)) {
                    sb.append((char) ch);
                    advance();
                }
                Integer st = bc.getSuit(sb.toString());
                if (st == null) {
                    throw new IllegalArgumentException("Unknown Suit: '" + sb + "'");
                }
                return () -> (short) (0xf & ~((1 << (st + 1)) - 1));
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
                        if (players.lho.infSummary.avgLenInSuit(suit) < 4 && players.rho.infSummary.avgLenInSuit(suit) < 4
                                && players.partner.infSummary.avgLenInSuit(suit) < 4 && players.me.infSummary.avgLenInSuit(suit) < 4) {
                            suits |= (1 << suit);
                        }
                    }
                    short suits2 = suits;
                    return () -> suits2;
                }
                case "MINORS":
                    return () -> MINORS;
                case "MAJORS":
                    return () -> MAJORS;
                case "REDS":
                    return () -> REDS;
                case "BLACKS":
                    return () -> BLACKS;
                case "ROUND":
                    return () -> ROUND;
                case "POINTED":
                    return () -> POINTED;
                case "ALL":
                    return () -> ALL_SUITS;
                case "NONE":
                    return () -> 0;
                default:
                    Integer st = bc.getSuit(sb.toString());
                    if (st != null) {
                        return () -> (short) (1 << st);
                    }
                    throw new IllegalArgumentException("Unknown Suit Set: '" + sb + "'");
                }
            }
        }

        public SuitSet lookupSuitSet() throws IOException {
            readWhite();
            SuitSet result = lookupSuitSet0();
            for (;;) {
                readWhite();
                if (ch != '&') {
                    break;
                }
                advance();
                SuitSet result2 = lookupSuitSet0();
                SuitSet result3 = result;
                result = () -> (short)(result3.evaluate() & result2.evaluate());
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
     * @param str
     *            The string to parse
     * @return The suits bound to the given expression
     */
    public SuitSet lookupSuitSet(String str) {
        try (ReadState state = new ReadState(str)) {
            SuitSet suits = state.lookupSuitSet();
            state.readWhite();
            if (state.ch != -1) {
                throw new IllegalArgumentException("Undecipherable characters at end");
            }
            return suits;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Suit Set: '" + str + "'", e);
        }
    }
}
