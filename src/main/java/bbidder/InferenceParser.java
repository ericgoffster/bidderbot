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
import bbidder.inferences.Preference;
import bbidder.inferences.Rebiddable;
import bbidder.inferences.RebiddableSecondSuit;
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
    private static final Map<String, Integer> POINT_RANGES = Map.of("min", 18, "inv", 22, "gf", 25, "slaminv", 31, "slam", 33, "grandinv", 35, "grand", 37);
    private static Pattern PATT_FIT = Pattern.compile("\\s*fit\\s*(.*)", Pattern.CASE_INSENSITIVE);
    private static Pattern PATT_PREF = Pattern.compile("\\s*prefer\\s*(.*)\\s*to\\s*(.*)", Pattern.CASE_INSENSITIVE);
    private static Pattern PATT_REBIDDABLE = Pattern.compile("\\s*rebiddable\\s*(.*)", Pattern.CASE_INSENSITIVE);
    private static Pattern PATT_REBIDDABLE_2nd_SUIT = Pattern.compile("\\s*rebiddable_2nd\\s+(.*)\\s+(.*)", Pattern.CASE_INSENSITIVE);
    private static Pattern PATT_SPECIFIC_CARDS = Pattern.compile("of\\s+top\\s+(\\d+)\\s+in\\s+(.*)", Pattern.CASE_INSENSITIVE);
    private static Pattern PATT_MIN_TO_MAX = Pattern.compile("\\s*(\\d+)\\s*\\-\\s*(\\d+)\\s*(.*)");
    private static Pattern PATT_MAX = Pattern.compile("\\s*(\\d+)\\s*\\-\\s*(.*)");
    private static Pattern PATT_MIN = Pattern.compile("\\s*(\\d+)\\s*\\+\\s*(.*)");
    private static Pattern PATT_EXACT = Pattern.compile("\\s*(\\d+)\\s*(.*)");
    
    /**
     * @param str
     *            The string to parse.
     * @return An inference for the string.
     */
    public static Inference parseInference(String str) {
        if (str == null) {
            return null;
        }
        int pos = str.indexOf(",");
        if (pos >= 0) {
            String str1 = str.substring(0, pos);
            String str2 = str.substring(pos + 1);
            return AndInference.create(parseInference(str1), parseInference(str2));
        }
        if (str.trim().equals("")) {
            return TrueInference.T;
        }
        if (str.trim().equalsIgnoreCase("balanced")) {
            return Balanced.BALANCED;
        }
        if (str.trim().equalsIgnoreCase("superbalanced")) {
            return VeryBalanced.VERY_BALANCED;
        }
        if (str.trim().equalsIgnoreCase("unbalanced")) {
            return UnBalanced.UNBALANCED;
        }
        {
            RangeOf rng = InferenceParser.parseRange(str);
            if (rng != null) {
                if (rng.of.equalsIgnoreCase("hcp")) {
                    return new HCPRange(PointRange.between(rng.min, rng.max));
                }
                if (rng.of.equalsIgnoreCase("tpts")) {
                    return new TotalPointsRange(PointRange.between(rng.min, rng.max));
                }
                Symbol sym = SymbolParser.parseSymbol(rng.of);
                if (sym != null) {
                    return new SuitRange(sym, SuitLengthRange.between(rng.min, rng.max));
                }
            }
        }
        {
            Inference i = InferenceParser.parseLongestOrEq(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseFitInSuit(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseRebiddable(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseRebiddable2ndSuit(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseCombinedTPts(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseSpecificCards(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseStoppersInSuits(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseAlways(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parsePreference(str);
            if (i != null) {
                return i;
            }
        }
        throw new IllegalArgumentException("unknown inference: '" + str + "'");
    }

    private static LongestOrEqual parseLongestOrEq(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (!str.toLowerCase().startsWith("longest_or_equal")) {
            return null;
        }
        str = str.substring(16).trim();
        int pos = str.indexOf("among");
        if (pos >= 0) {
            Symbol sym = SymbolParser.parseSymbol(str.substring(0, pos).trim());
            if (sym == null) {
                return null;
            }
            return new LongestOrEqual(sym, SuitSetParser.lookupSuitSet(str.substring(pos + 5).trim()));
        }
        Symbol sym = SymbolParser.parseSymbol(str);
        if (sym == null) {
            return null;
        }
        return new LongestOrEqual(sym, null);
    }

    private static Inference parseFitInSuit(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = InferenceParser.PATT_FIT.matcher(str);
        if (m.matches()) {
            Symbol sym = SymbolParser.parseSymbol(m.group(1).trim());
            if (sym == null) {
                return null;
            }
            return new FitInSuit(sym);
        }
        return null;
    }

    private static Inference parseRebiddable(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = InferenceParser.PATT_REBIDDABLE.matcher(str);
        if (m.matches()) {
            Symbol sym = SymbolParser.parseSymbol(m.group(1).trim());
            if (sym == null) {
                return null;
            }
            return new Rebiddable(sym);
        }
        return null;
    }

    private static Inference parseRebiddable2ndSuit(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = InferenceParser.PATT_REBIDDABLE_2nd_SUIT.matcher(str);
        if (m.matches()) {
            Symbol longer = SymbolParser.parseSymbol(m.group(1).trim());
            Symbol shorter = SymbolParser.parseSymbol(m.group(2).trim());
            if (longer == null || shorter == null) {
                return null;
            }
            return new RebiddableSecondSuit(longer, shorter);
        }
        return null;
    }

    private static CombinedTotalPointsRange parseCombinedTPts(String str) {
        if (str == null) {
            return null;
        }
        return InferenceParser.makeTPtsRange(str.trim());
    }

    private static CombinedTotalPointsRange makeTPtsRange(String str) {
        String[] parts = SplitUtil.split(str, "-", 2);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 1) {
            CombinedTotalPointsRange rlow = makeTPtsRange(parts[0]);
            CombinedTotalPointsRange rhigh = makeTPtsRange(parts[1]);
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
        PointRange createRange = InferenceParser.createTPtsRange(str);
        if (createRange == null) {
            return null;
        }
        return new CombinedTotalPointsRange(createRange);
    }

    private static Inference parseSpecificCards(String str) {
        if (str == null) {
            return null;
        }
        RangeOf rng = InferenceParser.parseRange(str);
        if (rng != null) {
            Matcher m = InferenceParser.PATT_SPECIFIC_CARDS.matcher(rng.of);
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
        return null;
    }

    private static Inference parseStoppersInSuits(String str) {
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("stoppers")) {
            return new StoppersInSuits(SuitSetParser.lookupSuitSet(parts[1]), false);
        }
        if (parts.length == 2 && parts[0].equalsIgnoreCase("partial_stoppers")) {
            return new StoppersInSuits(SuitSetParser.lookupSuitSet(parts[1]), true);
        }
        return null;
    }

    private static Always parseAlways(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("always")) {
            return new Always();
        }
        return null;
    }

    private static Inference parsePreference(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = InferenceParser.PATT_PREF.matcher(str);
        if (m.matches()) {
            Symbol sym1 = SymbolParser.parseSymbol(m.group(1).trim());
            if (sym1 == null) {
                return null;
            }
            Symbol sym2 = SymbolParser.parseSymbol(m.group(2).trim());
            if (sym2 == null) {
                return null;
            }
            return new Preference(sym1, sym2);
        }
        return null;
    }

    private static PointRange createTPtsRange(String str) {
        return InferenceParser.createTPtsRange(str, InferenceParser.POINT_RANGES);
    }

    private static PointRange createTPtsRange(String str, Map<String, Integer> m) {
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

    private static RangeOf parseRange(String str) {
        if (str == null) {
            return null;
        }
        Matcher m;
        m = InferenceParser.PATT_MIN_TO_MAX.matcher(str);
        if (m.matches()) {
            return new RangeOf(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), m.group(3).trim());
        }
    
        m = InferenceParser.PATT_MAX.matcher(str);
        if (m.matches()) {
            return new RangeOf(null, Integer.parseInt(m.group(1)), m.group(2).trim());
        }
        m = InferenceParser.PATT_MIN.matcher(str);
        if (m.matches()) {
            return new RangeOf(Integer.parseInt(m.group(1)), null, m.group(2).trim());
        }
        m = InferenceParser.PATT_EXACT.matcher(str);
        if (m.matches()) {
            return new RangeOf(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(1)), m.group(2).trim());
        }
        return null;
    }
}
