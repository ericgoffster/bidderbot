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
import java.util.Map;

import bbidder.suitsets.And;
import bbidder.suitsets.ConstSet;
import bbidder.suitsets.Gt;
import bbidder.suitsets.LookupSet;
import bbidder.suitsets.Not;
import bbidder.suitsets.Unbid;
import bbidder.suitsets.Unstopped;

public final class SuitSets {

    public static Symbol bind(SymbolTable symbols, Symbol symbol) {
        Map<Symbol, SymbolTable> boundOthers = symbol.resolveSymbol(symbols);
        if (boundOthers.size() != 1) {
            throw new IllegalArgumentException("undefined smybol: " + symbol);
        }
        Symbol evaluate = boundOthers.keySet().iterator().next();
        if (evaluate == null) {
            throw new IllegalArgumentException(symbol + " undefined");
        }
        return evaluate;
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
                Symbol symbol = SymbolParser.parseSymbol(strain);
                if (symbol == null) {
                    throw new IllegalArgumentException("bad symbol " + strain);
                }
                return new Gt(symbol);
            } else {
                StringBuilder sb = new StringBuilder();
                while (Character.isLetter(ch) || ch == '_' || Character.isDigit(ch)) {
                    sb.append((char) ch);
                    advance();
                }
                switch (sb.toString().toUpperCase()) {
                case "UNBID":
                    return new Unbid();
                case "UNSTOPPED":
                    return new Unstopped();
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
                    return new ConstSet(sb.toString().toUpperCase(), (short) 0);
                default:
                    Symbol symbol = SymbolParser.parseSymbol(sb.toString());
                    if (symbol == null) {
                        throw new IllegalArgumentException("bad symbol " + sb);
                    }
                    return new LookupSet(symbol);
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
