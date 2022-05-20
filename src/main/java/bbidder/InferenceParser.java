package bbidder;

import java.util.Map;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbidder.inferences.Always;
import bbidder.inferences.AndInference;
import bbidder.inferences.Balanced;
import bbidder.inferences.CombinedTotalPointsRange;
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
    private static final Map<String, Integer> POINT_RANGES = Map.of("min", 18, "inv", 22, "gf", 25, "slaminv", 31, "slam", 33, "grandinv", 35,
            "grand", 37);
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
                    if (rng.maxPromised) {
                        if (rng.of.equalsIgnoreCase(MaxHCPs.HCP)) {
                            return MaxHCPs.MAX_HCP_RANGE;
                        } else if (rng.of.equalsIgnoreCase(MaxTpts.TPTS)) {
                            return MaxTpts.MAX_TPTS_RANGE;
                        } else {
                            Symbol sym = SymbolParser.parseSymbol(rng.of);
                            if (sym != null) {
                                return new MaxSuitRange(sym);
                            }
                        }
                    } else if (rng.of.equalsIgnoreCase(HCPRange.HCP)) {
                        return new HCPRange(PointRange.between(rng.min, rng.max));
                    } else if (rng.of.equalsIgnoreCase(TotalPointsRange.TPTS)) {
                        return new TotalPointsRange(PointRange.between(rng.min, rng.max));
                    } else if (rng.of.startsWith("fit")) {
                        Symbol sym = SymbolParser.parseSymbol(rng.of.substring(3).trim());
                        if (sym != null) {
                            return new FitInSuit(sym, SuitLengthRange.between(rng.min, rng.max));
                        }
                    } else {
                        {
                            Symbol sym = SymbolParser.parseSymbol(rng.of);
                            if (sym != null) {
                                return new SuitRange(sym, SuitLengthRange.between(rng.min, rng.max));
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
                Inference i = makeCombinedTPtsRange(str.trim());
                if (i != null) {
                    return i;
                }
            }
            break;
        }
        }
        throw new IllegalArgumentException("unknown inference: '" + str + "'");
    }

    private static CombinedTotalPointsRange makeCombinedTPtsRange(String str) {
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 1) {
            CombinedTotalPointsRange rlow = makeCombinedTPtsRange(parts[0]);
            CombinedTotalPointsRange rhigh = makeCombinedTPtsRange(parts[1]);
            if (rlow == null || rhigh == null) {
                return null;
            }
            OptionalInt l = rlow.rng.lowest();
            OptionalInt h = rhigh.rng.highest();
            if (!l.isPresent()) {
                return new CombinedTotalPointsRange(PointRange.NONE);
            }
            if (!h.isPresent()) {
                return new CombinedTotalPointsRange(PointRange.NONE);
            }
            int lower = l.getAsInt();
            int higher = h.getAsInt();
            return new CombinedTotalPointsRange(PointRange.between(lower, higher));
        }
        PointRange createRange = createCombinedTPtsRange(str, POINT_RANGES);
        if (createRange == null) {
            return null;
        }
        return new CombinedTotalPointsRange(createRange);
    }

    private static PointRange createCombinedTPtsRange(String str, Map<String, Integer> m) {
        final int dir;
        if (str.endsWith("+")) {
            str = str.substring(0, str.length() - 1);
            dir = 1;
        } else if (str.endsWith("-")) {
            str = str.substring(0, str.length() - 1);
            dir = -1;
        } else {
            dir = 0;
        }

        if (!m.containsKey(str)) {
            return null;
        }

        Integer pts = m.get(str);
        if (dir > 0) {
            return PointRange.between(pts, null);
        }

        Integer maxPts = null;
        for (var e : m.entrySet()) {
            int nextMax = e.getValue() - 1;
            if (nextMax >= pts && (maxPts == null || nextMax < maxPts)) {
                maxPts = nextMax;
            }
        }

        if (dir < 0) {
            pts = null;
        }

        if (maxPts == null) {
            return PointRange.between(pts, null);
        }

        return PointRange.between(pts, maxPts);
    }
}
