package bbidder.parsers;

import bbidder.Generality;
import bbidder.RangeOf;
import bbidder.SuitLengthRange;
import bbidder.Symbol;
import bbidder.generalities.AndGenerality;
import bbidder.generalities.BestFitEstablished;
import bbidder.generalities.BidSuitGenerality;
import bbidder.generalities.FitEstablished;
import bbidder.generalities.IsTwoSuitedGenerality;
import bbidder.generalities.ConstGenerality;
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
        case ConstGenerality.NAME: {
            if (remainder.equals("")) {
                return ConstGenerality.T;
            }
            break;
        }
        case "i_bid_suit": {
            String[] rem = SplitUtil.split(remainder, "promising", 2);
            SuitLengthRange range;
            if (rem.length == 2) {
                RangeOf rng = RangeParser.parseRange(rem[1]);
                if (rng != null && rng.of.equals("")) {
                    range = SuitLengthRange.between(rng.min, rng.max);
                } else {
                    return null;
                }
            } else {
                range = SuitLengthRange.atLeast(3);
            }
            Symbol symbol = SymbolParser.parseSymbol(rem[0]);
            if (symbol != null) {
                return new BidSuitGenerality(symbol, 0, range);
            }
            break;
        }
        case "partner_bid_suit": {
            String[] rem = SplitUtil.split(remainder, "promising", 2);
            SuitLengthRange range;
            if (rem.length == 2) {
                RangeOf rng = RangeParser.parseRange(rem[1]);
                if (rng != null && rng.of.equals("")) {
                    range = SuitLengthRange.between(rng.min, rng.max);
                } else {
                    return null;
                }
            } else {
                range = SuitLengthRange.atLeast(3);
            }
            Symbol symbol = SymbolParser.parseSymbol(rem[0]);
            if (symbol != null) {
                return new BidSuitGenerality(symbol, 2, range);
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
        default: {
            RangeOf rng = RangeParser.parseRange(str.trim());
            if (rng != null) {
                if (rng.of.startsWith(FitEstablished.NAME)) {
                    Symbol sym = SymbolParser.parseSymbol(rng.of.substring(FitEstablished.NAME.length()).trim());
                    if (sym != null) {
                        return new FitEstablished(sym, SuitLengthRange.between(rng.min, rng.max));
                    }
                } else if (rng.of.startsWith(BestFitEstablished.NAME)) {
                    Symbol sym = SymbolParser.parseSymbol(rng.of.substring(BestFitEstablished.NAME.length()).trim());
                    if (sym != null) {
                        return new BestFitEstablished(sym, SuitLengthRange.between(rng.min, rng.max));
                    }
                }
            }
            break;
        }
        }
        throw new IllegalArgumentException("unknown generality: '" + str + "'");
    }
}
