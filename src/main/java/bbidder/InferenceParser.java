package bbidder;

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
            Inference i = Balanced.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = VeryBalanced.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = UnBalanced.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = HCPRange.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = LongestOrEqual.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = SuitRange.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = FitInSuit.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = Rebiddable.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = RebiddableSecondSuit.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = TotalPointsRange.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = CombinedTotalPointsRange.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = SpecificCards.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = StoppersInSuits.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = Always.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        {
            Inference i = Preference.valueOf(str);
            if (i != null) {
                return i;
            }
        }
        throw new IllegalArgumentException("unknown inference: '" + str + "'");
    }
}
