package bbidder;

import java.util.regex.Matcher;

public class SymbolParser {

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
        if (symbol.endsWith(":down")) {
            Symbol sym = parseSymbol(symbol.substring(0, symbol.length() - 5));
            if (sym == null) {
                return null;
            }
            return new DownSymbol(sym);
        }
        if (symbol.startsWith("~")) {
            Symbol sym = parseSymbol(symbol.substring(1));
            if (sym == null) {
                return null;
            }
            return new NotSymbol(sym);
        }
        if (symbol.equals("om")) {
            return new VarSymbol(symbol);
        }
        if (symbol.equals("OM")) {
            return new VarSymbol(symbol);
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
