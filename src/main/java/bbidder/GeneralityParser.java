package bbidder;

import bbidder.generalities.AndGenerality;
import bbidder.generalities.BestFitEstablished;
import bbidder.generalities.BidSuitGenerality;
import bbidder.generalities.FitEstablished;
import bbidder.generalities.IsTwoSuitedGenerality;
import bbidder.generalities.TrueGenerality;
import bbidder.generalities.UnbidSuitGenerality;
import bbidder.generalities.WeAreThreeSuited;
import bbidder.utils.SplitUtil;

/**
 * The registry of all possible inferences.
 *
 * @author goffster
 *
 */
public final class GeneralityParser {
    public static Generality parseGenerality(String str) {
        if (str == null) {
            return null;
        }
        int pos = str.indexOf(",");
        if (pos >= 0) {
            String str1 = str.substring(0, pos);
            String str2 = str.substring(pos + 1);
            return AndGenerality.create(parseGenerality(str1), parseGenerality(str2));
        }
        String tag;
        String remainder;
        {
            String s = str.trim();
            int p = 0;
            while (p < s.length() && !Character.isWhitespace(s.charAt(p))) {
                p++;
            }
            tag = s.substring(0, p).toLowerCase();
            remainder = s.substring(p).trim();
        }
        switch (tag) {
        case "":
        case TrueGenerality.NAME: {
            if (remainder.equals("")) {
                return TrueGenerality.T;
            }
            break;
        }
        case BestFitEstablished.NAME: {
            Symbol symbol = SymbolParser.parseSymbol(remainder);
            if (symbol != null) {
                return new BestFitEstablished(symbol, 7);
            }
            break;
        }
        case "bestfit7_established": {
            Symbol symbol = SymbolParser.parseSymbol(remainder);
            if (symbol != null) {
                return new BestFitEstablished(symbol, 7);
            }
            break;
        }
        case "bestfit8_established": {
            Symbol symbol = SymbolParser.parseSymbol(remainder);
            if (symbol != null) {
                return new BestFitEstablished(symbol, 8);
            }
            break;
        }
        case FitEstablished.NAME: {
            Symbol symbol = SymbolParser.parseSymbol(remainder);
            if (symbol != null) {
                return new FitEstablished(symbol, SuitLengthRange.atLeast(8));
            }
            break;
        }
        case "fit7_established": {
            Symbol symbol = SymbolParser.parseSymbol(remainder);
            if (symbol != null) {
                return new FitEstablished(symbol, SuitLengthRange.atLeast(7));
            }
            break;
        }
        case "fit8_established": {
            Symbol symbol = SymbolParser.parseSymbol(remainder);
            if (symbol != null) {
                return new FitEstablished(symbol, SuitLengthRange.atLeast(8));
            }
            break;
        }
        case "fit9_established": {
            Symbol symbol = SymbolParser.parseSymbol(remainder);
            if (symbol != null) {
                return new FitEstablished(symbol, SuitLengthRange.atLeast(9));
            }
            break;
        }
        case "fit10_established": {
            Symbol symbol = SymbolParser.parseSymbol(remainder);
            if (symbol != null) {
                return new FitEstablished(symbol, SuitLengthRange.atLeast(10));
            }
            break;
        }
        case "i_bid_suit": {
            String[] rem = SplitUtil.split(remainder, "at_least", 2);
            int min = rem.length > 1 ? Integer.parseInt(rem[1]) : 3;
            Symbol symbol = SymbolParser.parseSymbol(rem[0]);
            if (symbol != null) {
                return new BidSuitGenerality(symbol, 0, min);
            }
            break;
        }
        case "partner_bid_suit": {
            String[] rem = SplitUtil.split(remainder, "at_least", 2);
            int min = rem.length > 1 ? Integer.parseInt(rem[1]) : 3;
            Symbol symbol = SymbolParser.parseSymbol(rem[0]);
            if (symbol != null) {
                return new BidSuitGenerality(symbol, 2, min);
            }
            break;
        }
        case UnbidSuitGenerality.NAME: {
            Symbol symbol = SymbolParser.parseSymbol(remainder);
            if (symbol != null) {
                return new UnbidSuitGenerality(symbol);
            }
            break;
        }
        case WeAreThreeSuited.NAME: {
            if (remainder.equals("")) {
                return WeAreThreeSuited.WE_R_3_SUITED;
            }
            break;
        }
        case "i_am_two_suited": {
            String[] symbols = SplitUtil.split(remainder, "\\s+", 2);
            if (symbols.length == 2) {
                Symbol longer = SymbolParser.parseSymbol(symbols[0]);
                Symbol shorter = SymbolParser.parseSymbol(symbols[1]);
                if (longer != null && shorter != null) {
                    return new IsTwoSuitedGenerality(longer, shorter, 0);
                }
            }
            break;
        }
        case "partner_is_two_suited": {
            String[] symbols = SplitUtil.split(remainder, "\\s+", 2);
            if (symbols.length == 2) {
                Symbol longer = SymbolParser.parseSymbol(symbols[0]);
                Symbol shorter = SymbolParser.parseSymbol(symbols[1]);
                if (longer != null && shorter != null) {
                    return new IsTwoSuitedGenerality(longer, shorter, 2);
                }
            }
            break;
        }
        default:
            break;
        }
        throw new IllegalArgumentException("unknown generality: '" + str + "'");
    }
}
