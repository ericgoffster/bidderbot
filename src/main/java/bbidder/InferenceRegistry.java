package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * The registry of all possible inferences.
 *
 * @author goffster
 *
 */
public class InferenceRegistry {
    final List<Function<String, Inference>> items = new ArrayList<>();

    /**
     * Add a constructor of an inference from a string
     * 
     * @param constructor String => Inference
     */
    public void add(Function<String, Inference> constructor) {
        items.add(constructor);
    }

    /**
     * @param str The string to parse.
     * @return An inference for the string.
     */
    public Inference valueOf(String str) {
        for (Function<String, Inference> item : items) {
            Inference inf = item.apply(str);
            if (inf != null) {
                return inf;
            }
        }
        throw new IllegalArgumentException("unknown inference: '" + str + "'");
    }
}
