package bbidder;

import java.util.OptionalInt;
import java.util.regex.Matcher;

import bbidder.inferences.Always;
import bbidder.inferences.AndInference;
import bbidder.inferences.Balanced;
import bbidder.inferences.CombinedTotalPointsRange;
import bbidder.inferences.FitInSuit;
import bbidder.inferences.HCPRange;
import bbidder.inferences.LongestOrEqual;
import bbidder.inferences.Preference;
import bbidder.inferences.RangeOf;
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
        {
            Inference i = InferenceParser.parseBalanced(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseVeryBalanced(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseUnbalanced(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseHCPRange(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseLongestOrEq(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = InferenceParser.parseSuitRange(str);
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
            Inference i = InferenceParser.parseTPtsRange(str);
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

    public static Balanced parseBalanced(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("balanced")) {
            return Balanced.BALANCED;
        }
        return null;
    }

    public static VeryBalanced parseVeryBalanced(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("superbalanced")) {
            return VeryBalanced.VERY_BALANCED;
        }
        return null;
    }

    public static UnBalanced parseUnbalanced(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("unbalanced")) {
            return UnBalanced.UNBALANCED;
        }
        return null;
    }

    public static Inference parseHCPRange(String str) {
        RangeOf rng = RangeOf.valueOf(str);
        if (rng == null) {
            return null;
        }
        if (rng.of.equalsIgnoreCase("hcp")) {
            return new HCPRange(PointRange.between(rng.min, rng.max));
        } else {
            return null;
        }
    }

    public static LongestOrEqual parseLongestOrEq(String str) {
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

    public static Inference parseSuitRange(String str) {
        if (str == null) {
            return null;
        }
        RangeOf rng = RangeOf.valueOf(str);
        if (rng == null) {
            return null;
        }
        Symbol sym = SymbolParser.parseSymbol(rng.of);
        if (sym != null) {
            return new SuitRange(sym, SuitLengthRange.between(rng.min, rng.max));
        } else {
            return null;
        }
    }

    public static Inference parseFitInSuit(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = FitInSuit.PATT_FIT.matcher(str);
        if (m.matches()) {
            Symbol sym = SymbolParser.parseSymbol(m.group(1).trim());
            if (sym == null) {
                return null;
            }
            return new FitInSuit(sym);
        }
        return null;
    }

    public static Inference parseRebiddable(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = Rebiddable.PATT_FIT.matcher(str);
        if (m.matches()) {
            Symbol sym = SymbolParser.parseSymbol(m.group(1).trim());
            if (sym == null) {
                return null;
            }
            return new Rebiddable(sym);
        }
        return null;
    }

    public static Inference parseRebiddable2ndSuit(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = RebiddableSecondSuit.PATT_FIT.matcher(str);
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

    public static Inference parseTPtsRange(String str) {
        RangeOf rng = RangeOf.valueOf(str);
        if (rng == null) {
            return null;
        }
        if (rng.of.equalsIgnoreCase("tpts")) {
            return new TotalPointsRange(PointRange.between(rng.min, rng.max));
        } else {
            return null;
        }
    }

    public static CombinedTotalPointsRange parseCombinedTPts(String str) {
        if (str == null) {
            return null;
        }
        return InferenceParser.makeTPtsRange(str.trim());
    }

    public static CombinedTotalPointsRange makeTPtsRange(String str) {
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
        PointRange createRange = CombinedTotalPointsRange.createRange(str);
        if (createRange == null) {
            return null;
        }
        return new CombinedTotalPointsRange(createRange);
    }

    public static Inference parseSpecificCards(String str) {
        if (str == null) {
            return null;
        }
        RangeOf rng = RangeOf.valueOf(str);
        if (rng != null) {
            Matcher m = SpecificCards.PATT.matcher(rng.of);
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

    public static Inference parseStoppersInSuits(String str) {
        String[] parts = SplitUtil.split(str, "\\s+", 2);
        if (parts.length == 2 && parts[0].equalsIgnoreCase("stoppers")) {
            return new StoppersInSuits(SuitSetParser.lookupSuitSet(parts[1]), false);
        }
        if (parts.length == 2 && parts[0].equalsIgnoreCase("partial_stoppers")) {
            return new StoppersInSuits(SuitSetParser.lookupSuitSet(parts[1]), true);
        }
        return null;
    }

    public static Always parseAlways(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.toLowerCase().equals("always")) {
            return new Always();
        }
        return null;
    }

    public static Inference parsePreference(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = Preference.PATT_FIT.matcher(str);
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
}
