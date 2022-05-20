package bbidder.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.Strain;
import bbidder.Symbol;
import bbidder.symbols.ConstSymbol;
import bbidder.symbols.DownSymbol;
import bbidder.symbols.GreaterThanSymbol;
import bbidder.symbols.LessThanSymbol;
import bbidder.symbols.MajorSymbol;
import bbidder.symbols.MinorSymbol;
import bbidder.symbols.OtherMajorSymbol;
import bbidder.symbols.OtherMinorSymbol;
import bbidder.symbols.SeatSymbol;
import bbidder.symbols.SteppedSymbol;
import bbidder.symbols.TagSymbol;
import bbidder.symbols.VarSymbol;

/**
 * Parses a Symbol
 * 
 * @author goffster
 *
 */
public final class SymbolParser {
    private static Pattern SUIT_PATTERN = Pattern.compile("(.*)\\-(\\d+)");
    private static Pattern LESS_THAN = Pattern.compile("<(\\d+)(.+)");
    private static Pattern GREATER_THAN = Pattern.compile(">(\\d+)(.+)");
    private static Pattern SEATS = Pattern.compile("seats(\\d+)");

    /**
     * @param str
     *            The symbol to test
     * @return true, if the symbol is syntactically a valid suit.
     */
    public static Symbol parseSymbol(String str) {
        {
            Matcher m = SUIT_PATTERN.matcher(str);
            if (m.matches()) {
                Symbol symbol = parseSymbol(m.group(1));
                if (symbol == null) {
                    return null;
                }
                return new SteppedSymbol(symbol, Integer.parseInt(m.group(2)));
            }
        }
        int pos = str.lastIndexOf(":");
        if (pos >= 0) {
            Symbol symbol = parseSymbol(str.substring(0, pos));
            if (symbol == null) {
                return null;
            }
            String tag = str.substring(pos + 1);
            if (tag.startsWith("\"") && tag.endsWith("\"")) {
                return new TagSymbol(tag.substring(1, tag.length() - 1), symbol);
            } 
            if (tag.equalsIgnoreCase(DownSymbol.NAME)) {
                return new DownSymbol(symbol);
            }
            {
                Matcher m = SEATS.matcher(tag);
                if (m.matches()) {
                    short seats = 0;
                    String seatsC = m.group(1);
                    for (int i = 0; i < seatsC.length(); i++) {
                        seats |= (1 << (seatsC.charAt(i) - '1'));
                    }
                    return new SeatSymbol(symbol, seats);
                }
            }
            {
                Matcher m = LESS_THAN.matcher(tag);
                if (m.matches()) {
                    Symbol other = parseSymbol(m.group(2));
                    if (other == null) {
                        return null;
                    }
                    return new LessThanSymbol(symbol, Integer.parseInt(m.group(1)) - 1, other);
                }
            }
            {
                Matcher m = GREATER_THAN.matcher(tag);
                if (m.matches()) {
                    Symbol other = parseSymbol(m.group(2));
                    if (other == null) {
                        return null;
                    }
                    return new GreaterThanSymbol(symbol, Integer.parseInt(m.group(1)) - 1, other);
                }
            }
            return null;
        }
        if (str.equals(OtherMinorSymbol.NAME)) {
            return new OtherMinorSymbol();
        }
        if (str.equals(OtherMajorSymbol.NAME)) {
            return new OtherMajorSymbol();
        }
        if (str.equals(MinorSymbol.NAME)) {
            return new MinorSymbol();
        }
        if (str.equals(MajorSymbol.NAME)) {
            return new MajorSymbol();
        }
        Integer strain = Strain.getStrain(str);
        if (strain != null) {
            return new ConstSymbol(strain);
        }
        if (str.length() == 1) {
            return new VarSymbol(str);
        }
        return null;
    }

}
