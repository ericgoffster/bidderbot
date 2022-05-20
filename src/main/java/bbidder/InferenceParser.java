package bbidder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.inferences.Always;
import bbidder.inferences.AndInference;
import bbidder.inferences.Balanced;
import bbidder.inferences.FitInSuit;
import bbidder.inferences.HCPRange;
import bbidder.inferences.LongestOrEqual;
import bbidder.inferences.MaxHCPs;
import bbidder.inferences.MaxSuitRange;
import bbidder.inferences.MaxTpts;
import bbidder.inferences.Preference;
import bbidder.inferences.Rebiddable;
import bbidder.inferences.RebiddableSecondSuit;
import bbidder.inferences.SemiBalanced;
import bbidder.inferences.SpecificCards;
import bbidder.inferences.StoppersInSuits;
import bbidder.inferences.SuitRange;
import bbidder.inferences.TotalPointsRange;
import bbidder.inferences.TrueInference;
import bbidder.inferences.UnBalanced;
import bbidder.inferences.VeryBalanced;
import bbidder.utils.SplitUtil;

/**
 * The registry of all possible inferences.
 *
 * @author goffster
 *
 */
public final class InferenceParser {
    private static Pattern PATT_SPECIFIC_CARDS = Pattern.compile("of\\s+top\\s+(\\d+)\\s+in\\s+(.*)", Pattern.CASE_INSENSITIVE);
    /**
     * @param str
     *            The string to parse.
     * @return An inference for the string.
     */
    public static Inference parseInference(String str) {
        if (str == null) {
            return null;
        }
        {
            int pos = str.indexOf(",");
            if (pos >= 0) {
                String str1 = str.substring(0, pos);
                String str2 = str.substring(pos + 1);
                return AndInference.create(parseInference(str1), parseInference(str2));
            }
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
        case TrueInference.NAME:
            if (remainder.equals("")) {
                return TrueInference.T;
            }
            break;
        case Balanced.NAME:
            if (remainder.equals("")) {
                return Balanced.BALANCED;
            }
            break;
        case SemiBalanced.NAME:
            if (remainder.equals("")) {
                return SemiBalanced.SEMI_BALANCED;
            }
            break;
        case VeryBalanced.NAME:
            if (remainder.equals("")) {
                return VeryBalanced.VERY_BALANCED;
            }
            break;
        case UnBalanced.NAME:
            if (remainder.equals("")) {
                return UnBalanced.UNBALANCED;
            }
            break;
        case Always.NAME:
            if (remainder.equals("")) {
                return Always.ALWAYS;
            }
            break;
        case Rebiddable.NAME: {
            Symbol sym = SymbolParser.parseSymbol(remainder.trim());
            if (sym != null) {
                return new Rebiddable(sym);
            }
            break;
        }
        case LongestOrEqual.NAME: {
            String[] parts = SplitUtil.split(remainder, "\\s*among\\s*");
            if (parts.length == 2) {
                Symbol sym = SymbolParser.parseSymbol(parts[0]);
                if (sym != null) {
                    return new LongestOrEqual(sym, SuitSetParser.lookupSuitSet(parts[1]));
                }
            } else if (parts.length == 1) {
                Symbol sym = SymbolParser.parseSymbol(parts[0]);
                if (sym != null) {
                    return new LongestOrEqual(sym, null);
                }
            }
            break;
        }
        case RebiddableSecondSuit.NAME: {
            String[] symbols = SplitUtil.split(remainder, "\\s+", 2);
            if (symbols.length == 2) {
                Symbol longer = SymbolParser.parseSymbol(symbols[0]);
                Symbol shorter = SymbolParser.parseSymbol(symbols[1]);
                if (longer != null && shorter != null) {
                    return new RebiddableSecondSuit(longer, shorter);
                }
            }
            break;
        }
        case StoppersInSuits.FULL:
            return new StoppersInSuits(SuitSetParser.lookupSuitSet(remainder), false);
        case StoppersInSuits.PARTIAL:
            return new StoppersInSuits(SuitSetParser.lookupSuitSet(remainder), true);
        case Preference.NAME: {
            String[] parts = SplitUtil.split(remainder, "\\s*to\\s*");
            if (parts.length == 2) {
                Symbol sym1 = SymbolParser.parseSymbol(parts[0]);
                if (sym1 != null) {
                    Symbol sym2 = SymbolParser.parseSymbol(parts[1]);
                    if (sym2 != null) {
                        return new Preference(sym1, sym2);
                    }
                }
            }
            break;
        }
        default: {
            {
                RangeOf rng = RangeParser.parseRange(str.trim());
                if (rng != null) {
                    if (rng.of.equalsIgnoreCase(HCPRange.HCP)) {
                        if (rng.maxPromised) {
                            return MaxHCPs.MAX_HCP_RANGE;
                        } else {
                            return new HCPRange(PointRange.between(rng.min, rng.max));
                        }
                    } else if (rng.of.equalsIgnoreCase(TotalPointsRange.TPTS)) {
                        if (rng.maxPromised) {
                            return MaxTpts.MAX_TPTS_RANGE;
                        } else {
                            return new TotalPointsRange(PointRange.between(rng.min, rng.max));
                        }
                    } else if (rng.of.startsWith("fit")) {
                        Symbol sym = SymbolParser.parseSymbol(rng.of.substring(3).trim());
                        if (sym != null) {
                            return new FitInSuit(sym, SuitLengthRange.between(rng.min, rng.max));
                        }
                    } else {
                        {
                            Symbol sym = SymbolParser.parseSymbol(rng.of);
                            if (sym != null) {
                                if (rng.maxPromised) {
                                    return new MaxSuitRange(sym);
                                } else {
                                    return new SuitRange(sym, SuitLengthRange.between(rng.min, rng.max));
                                }
                            }
                        }
                        {
                            Matcher m = PATT_SPECIFIC_CARDS.matcher(rng.of);
                            if (m.matches()) {
                                int top = Integer.parseInt(m.group(1));
                                String suit = m.group(2);
                                Symbol sym = SymbolParser.parseSymbol(suit);
                                if (sym == null) {
                                    return null;
                                }
                                return new SpecificCards(sym, CardsRange.between(rng.min, rng.max), top);
                            }
                        }
                    }
                }
            }
            {
                Inference i = CombinedPointsRangeParser.makeCombinedTPtsRange(str.trim());
                if (i != null) {
                    return i;
                }
            }
            break;
        }
        }
        throw new IllegalArgumentException("unknown inference: '" + str + "'");
    }
}
