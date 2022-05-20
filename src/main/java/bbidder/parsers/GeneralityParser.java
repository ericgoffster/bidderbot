package bbidder.parsers;

import java.io.IOException;
import java.io.StringReader;

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
            Position position = Position.getPosition(tag);
            if (remainder.startsWith("bid")) {
                String[] rem = SplitUtil.split(remainder.substring(3), "promising", 2);
                SuitLengthRange range;
                if (rem.length == 2) {
                    try(Input inp = new Input(new StringReader(rem[1]))) {
                        RangeOf rng = new RangeParser().parse(inp);
                        if (rng == null) {
                            throw new IllegalArgumentException();
                        }
                        range = SuitLengthRange.between(rng.min, rng.max);
                    } catch (IOException e) {
                        return null;
                    }
                } else {
                    range = SuitLengthRange.atLeast(0);
                }
                Symbol symbol = SymbolParser.parseSymbol(rem[0]);
                if (symbol != null) {
                    return new BidSuitGenerality(symbol, position, range);
                }
                break;
            }
            if (remainder.startsWith("made_bid")) {
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
            try (Input inp = new Input(new StringReader(str.trim()))) {
                RangeOf rng = new RangeParser().parse(inp);
                if (rng != null) {
                    inp.advanceWhite();
                    if (inp.readKeyword(FitEstablished.NAME.toUpperCase())) {
                        String sy = inp.readToken(ch -> true);
                        Symbol sym = SymbolParser.parseSymbol(sy);
                        if (sym != null) {
                            return new FitEstablished(sym, SuitLengthRange.between(rng.min, rng.max));
                        }
                    } else if (inp.readKeyword(BestFitEstablished.NAME.toUpperCase())) {
                        String sy = inp.readToken(ch -> true);
                        Symbol sym = SymbolParser.parseSymbol(sy);
                        if (sym != null) {
                            return new BestFitEstablished(sym, SuitLengthRange.between(rng.min, rng.max));
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                String rest = inp.readAny(ch->true);
                PointRange createRange = CombinedPointsRangeParser.parseCombinedTPtsRange(rest.trim());
                if (createRange != null) {
                    return new TotalPointsEstablished(createRange);
                }
            } catch (IOException e) {
            }
            break;
        }
        }
        throw new IllegalArgumentException("unknown generality: '" + str + "'");
    }
}
