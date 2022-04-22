package bbidder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.symbols.ConstSymbol;
import bbidder.symbols.DownSymbol;
import bbidder.symbols.GreaterThanSymbol;
import bbidder.symbols.LessThanSymbol;
import bbidder.symbols.MajorSymbol;
import bbidder.symbols.MinorSymbol;
import bbidder.symbols.NonConventional;
import bbidder.symbols.OtherMajorSymbol;
import bbidder.symbols.OtherMinorSymbol;
import bbidder.symbols.SteppedSymbol;
import bbidder.symbols.VarSymbol;

public final class SymbolParser {
    static Pattern SUIT_PATTERN = Pattern.compile("(.*)\\-(\\d+)");
    public static Pattern LESS_THAN = Pattern.compile("<(\\d+)(.+)");
    public static Pattern GREATER_THAN = Pattern.compile(">(\\d+)(.+)");

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
            if (tag.equalsIgnoreCase("down")) {
                return new DownSymbol(symbol);
            }
            if (tag.equalsIgnoreCase("nonconventional")) {
                return new NonConventional(symbol);
            }
            {
                Matcher m = LESS_THAN.matcher(tag);
                if (m.matches()) {
                    Symbol other = parseSymbol(m.group(2));
                    return new LessThanSymbol(symbol, Integer.parseInt(m.group(1)) - 1, other);
                }
            }
            {
                Matcher m = GREATER_THAN.matcher(tag);
                if (m.matches()) {
                    Symbol other = parseSymbol(m.group(2));
                    return new GreaterThanSymbol(symbol, Integer.parseInt(m.group(1)) - 1, other);
                }
            }
            return null;
        }
        if (str.equals("om")) {
            return new OtherMinorSymbol();
        }
        if (str.equals("OM")) {
            return new OtherMajorSymbol();
        }
        if (str.equals("m")) {
            return new MinorSymbol();
        }
        if (str.equals("M")) {
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
