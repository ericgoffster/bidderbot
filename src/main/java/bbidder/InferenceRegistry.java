package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import bbidder.generalities.AndGenerality;
import bbidder.generalities.TrueGenerality;
import bbidder.inferences.AndInference;
import bbidder.inferences.TrueInference;

/**
 * The registry of all possible inferences.
 *
 * @author goffster
 *
 */
public class InferenceRegistry {
    final List<Function<String, Inference>> inferences = new ArrayList<>();
    final List<Function<String, Generality>> generaliities = new ArrayList<>();

    /**
     * Add a constructor of an inference from a string
     * 
     * @param constructor
     *            String => Inference
     */
    public void addInference(Function<String, Inference> constructor) {
        inferences.add(constructor);
    }
    
    public void addGenerality(Function<String, Generality> constructor) {
        generaliities.add(constructor);
    }

    /**
     * @param str
     *            The string to parse.
     * @return An inference for the string.
     */
    public Inference parseInference(String str) {
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
        for (Function<String, Inference> item : inferences) {
            Inference inf = item.apply(str);
            if (inf != null) {
                return inf;
            }
        }
        throw new IllegalArgumentException("unknown inference: '" + str + "'");
    }
    
    public Generality parseGenerality(String str) {
        if (str == null) {
            return null;
        }
        int pos = str.indexOf(",");
        if (pos >= 0) {
            String str1 = str.substring(0, pos);
            String str2 = str.substring(pos + 1);
            return AndGenerality.create(parseGenerality(str1), parseGenerality(str2));
        }
        if (str.trim().equals("")) {
            return TrueGenerality.T;
        }
        for (Function<String, Generality> item : generaliities) {
            Generality inf = item.apply(str);
            if (inf != null) {
                return inf;
            }
        }
        throw new IllegalArgumentException("unknown generality: '" + str + "'");
    }
}
