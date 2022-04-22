package bbidder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.symbols.ConstSymbol;
import bbidder.symbols.DownSymbol;
import bbidder.symbols.GreaterThanSymbol;
import bbidder.symbols.LessThanSymbol;
import bbidder.symbols.MajorSymbol;
import bbidder.symbols.MinorSymbol;
import bbidder.symbols.NotSymbol;
import bbidder.symbols.OtherMajorSymbol;
import bbidder.symbols.OtherMinorSymbol;
import bbidder.symbols.SteppedSymbol;
import bbidder.symbols.VarSymbol;

public class SymbolParser {
    
    public static Pattern LESS_THAN = Pattern.compile("<(\\d+)(.+)");
    public static Pattern GREATER_THAN = Pattern.compile(">(\\d+)(.+)");

    /**
     * @param symbol
     *            The symbol to test
     * @return true, if the symbol is syntactically a valid suit.
     */
    public static Symbol parseSymbol(String symbol) {
        {
            Matcher m = BiddingContext.SUIT_PATTERN.matcher(symbol);
            if (m.matches()) {
                Symbol sym = parseSymbol(m.group(1));
                if (sym == null) {
                    return null;
                }
                return new SteppedSymbol(sym, Integer.parseInt(m.group(2)));
            }
        }
        int pos = symbol.lastIndexOf(":");
        if (pos >= 0) {
            Symbol sym = parseSymbol(symbol.substring(0, pos));
            if (sym == null) {
                return null;
            }
            String tag = symbol.substring(pos + 1);
            if (tag.toLowerCase().equals("down")) {
                return new DownSymbol(sym);
            }
            {
                Matcher m = LESS_THAN.matcher(tag);
                if (m.matches()) {
                    Symbol other = parseSymbol(m.group(2));
                    return new LessThanSymbol(sym, Integer.parseInt(m.group(1)) - 1, other);
                }
            }
            {
                Matcher m = GREATER_THAN.matcher(tag);
                if (m.matches()) {
                    Symbol other = parseSymbol(m.group(2));
                    return new GreaterThanSymbol(sym, Integer.parseInt(m.group(1)) - 1, other);
                }
            }
            return null;
        } 
        if (symbol.startsWith("~")) {
            Symbol sym = parseSymbol(symbol.substring(1));
            if (sym == null) {
                return null;
            }
            return new NotSymbol(sym);
        }
        if (symbol.equals("om")) {
            return new OtherMinorSymbol();
        }
        if (symbol.equals("OM")) {
            return new OtherMajorSymbol();
        }
        if (symbol.equals("m")) {
            return new MinorSymbol();
        }
        if (symbol.equals("M")) {
            return new MajorSymbol();
        }
        Integer strain = Strain.getStrain(symbol);
        if (strain != null) {
            return new ConstSymbol(strain);
        }
        if (symbol.length() == 1) {
            return new VarSymbol(symbol);
        }
        return null;
    }

}
