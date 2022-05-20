package bbidder.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.Strain;
import bbidder.Symbol;
import bbidder.symbols.ConstSymbol;
import bbidder.symbols.MajorSymbol;
import bbidder.symbols.MinorSymbol;
import bbidder.symbols.OtherMajorSymbol;
import bbidder.symbols.OtherMinorSymbol;
import bbidder.symbols.SteppedSymbol;
import bbidder.symbols.VarSymbol;

/**
 * Parses a Symbol
 * 
 * @author goffster
 *
 */
public final class SymbolParser {
    private static Pattern SUIT_PATTERN = Pattern.compile("(.*)\\-(\\d+)");

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
