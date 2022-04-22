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
import java.util.Objects;

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
        public short evaluate(InferenceContext inf);
    }

    public static class Not implements SuitSet {
        final SuitSet ss;

        public Not(SuitSet ss) {
            super();
            this.ss = ss;
        }

        @Override
        public short evaluate(InferenceContext inf) {
            return (short) (0xf & ~ss.evaluate(inf));
        }

        @Override
        public int hashCode() {
            return Objects.hash(ss);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Not other = (Not) obj;
            return Objects.equals(ss, other.ss);
        }
        @Override
        public String toString() {
            return "~"+ss;
        }
    }

    public static class Gt implements SuitSet {
        final String strain;

        public Gt(String strain) {
            super();
            this.strain = strain;
        }

        @Override
        public short evaluate(InferenceContext inf) {
            Integer st = inf.bc.getSuit(strain);
            if (st == null) {
                throw new IllegalArgumentException("Unknown Suit: '" + strain + "'");
            }
            return (short) (0xf & ~((1 << (st + 1)) - 1));
        }

        @Override
        public int hashCode() {
            return Objects.hash(strain);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Gt other = (Gt) obj;
            return Objects.equals(strain, other.strain);
        }

        @Override
        public String toString() {
            return ">"+strain;
        }
    }
    
    public static class Unbid implements SuitSet {
        public Unbid() {
            super();
        }

        @Override
        public short evaluate(InferenceContext inf) {
            short suits = 0;
            for (int suit = 0; suit < 4; suit++) {
                if (inf.players.lho.infSummary.avgLenInSuit(suit) < 4 && inf.players.rho.infSummary.avgLenInSuit(suit) < 4
                        && inf.players.partner.infSummary.avgLenInSuit(suit) < 4 && inf.players.me.infSummary.avgLenInSuit(suit) < 4) {
                    suits |= (1 << suit);
                }
            }
            return suits;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            return true;
        }
    }
    
    public static class ConstSet implements SuitSet {
        String ssuits;
        short suits;
        public ConstSet(String ssuits, short suits) {
            this.ssuits = ssuits;
            this.suits = suits;
        }

        @Override
        public short evaluate(InferenceContext inf) {
            return suits;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ssuits, suits);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ConstSet other = (ConstSet) obj;
            return Objects.equals(ssuits, other.ssuits) && suits == other.suits;
        }
        @Override
        public String toString() {
            return ssuits;
        }
    }

    public static class LookupSet implements SuitSet {
        String strain;
        public LookupSet(String strain) {
            this.strain = strain;
        }

        @Override
        public short evaluate(InferenceContext inf) {
            Integer st = inf.bc.getSuit(strain);
            if (st != null) {
                return (short) (1 << st);
            }
            throw new IllegalArgumentException("Unknown Suit Set: '" + strain + "'");
        }

        @Override
        public int hashCode() {
            return Objects.hash(strain);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            LookupSet other = (LookupSet) obj;
            return Objects.equals(strain, other.strain);
        }
        @Override
        public String toString() {
            return strain;
        }
    }
    
    public static class And implements SuitSet {
        SuitSet s1;
        SuitSet s2;
        public And(SuitSet s1, SuitSet s2) {
            super();
            this.s1 = s1;
            this.s2 = s2;
        }
        @Override
        public short evaluate(InferenceContext inf) {
            return (short)(s1.evaluate(inf) & s2.evaluate(inf));
        }
        @Override
        public int hashCode() {
            return Objects.hash(s1, s2);
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            And other = (And) obj;
            return Objects.equals(s1, other.s1) && Objects.equals(s2, other.s2);
        }
        @Override
        public String toString() {
            return s1 + " & " + s2;
        }
    }

    /**
     * Represents the parser state.
     * 
     * @author goffster
     *
     */
    private static class ReadState implements Closeable {
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
                return new Not(lookupSuitSet0());
            } else if (ch == '>') {
                advance();
                StringBuilder sb = new StringBuilder();
                while (Character.isLetter(ch) || ch == '_' || Character.isDigit(ch)) {
                    sb.append((char) ch);
                    advance();
                }
                String strain = sb.toString();
                return new Gt(strain);
            } else {
                StringBuilder sb = new StringBuilder();
                while (Character.isLetter(ch) || ch == '_' || Character.isDigit(ch)) {
                    sb.append((char) ch);
                    advance();
                }
                switch (sb.toString().toUpperCase()) {
                case "UNBID":
                    return new Unbid();
                case "MINORS":
                    return new ConstSet(sb.toString().toUpperCase(), MINORS);
                case "MAJORS":
                    return new ConstSet(sb.toString().toUpperCase(), MAJORS);
                case "REDS":
                    return new ConstSet(sb.toString().toUpperCase(), REDS);
                case "BLACKS":
                    return new ConstSet(sb.toString().toUpperCase(), BLACKS);
                case "ROUND":
                    return new ConstSet(sb.toString().toUpperCase(), ROUND);
                case "POINTED":
                    return new ConstSet(sb.toString().toUpperCase(), POINTED);
                case "ALL":
                    return new ConstSet(sb.toString().toUpperCase(), ALL_SUITS);
                case "NONE":
                    return new ConstSet(sb.toString().toUpperCase(), (short)0);
                default:
                    return new LookupSet(sb.toString());
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
                result = new And(result, result2);
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
    public static SuitSet lookupSuitSet(String str) {
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
