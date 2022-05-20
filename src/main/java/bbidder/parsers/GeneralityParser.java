package bbidder.parsers;

import bbidder.Generality;
import bbidder.PointRange;
import bbidder.Position;
import bbidder.RangeOf;
import bbidder.SuitLengthRange;
import bbidder.Symbol;
import bbidder.generalities.AndGenerality;
import bbidder.generalities.BestFitEstablished;
import bbidder.generalities.BidSuitGenerality;
import bbidder.generalities.ConstGenerality;
import bbidder.generalities.FitEstablished;
import bbidder.generalities.MadeBid;
import bbidder.generalities.TotalPointsEstablished;
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
        case "i":
        case "partner":
            if (remainder.startsWith("bid")) {
                String[] rem = SplitUtil.split(remainder.substring(3), "promising", 2);
                SuitLengthRange range;
                if (rem.length == 2) {
                    RangeOf rng = RangeParser.parseRange(rem[1]);
                    if (rng != null && rng.of.equals("")) {
                        range = SuitLengthRange.between(rng.min, rng.max);
                    } else {
                        return null;
                    }
                } else {
                    range = SuitLengthRange.atLeast(0);
                }
                Symbol symbol = SymbolParser.parseSymbol(rem[0]);
                if (symbol != null) {
                    return new BidSuitGenerality(symbol, Position.getPosition(tag), range);
                }
                break;
            }
            if (remainder.startsWith("made_bid")) {
                int position = tag.equals("i") ? 4 : 2;
                return new MadeBid(position, remainder.substring(8).trim());
            }
            break;
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
            {
                PointRange createRange = CombinedPointsRangeParser.parseCombinedTPtsRange(str.trim());
                if (createRange != null) {
                    return new TotalPointsEstablished(createRange);
                }
            }
            break;
        }
        }
        throw new IllegalArgumentException("unknown generality: '" + str + "'");
    }
}
