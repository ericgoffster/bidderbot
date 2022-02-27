package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class InferenceRegistry {
    final List<Function<String, Inference>> items = new ArrayList<>();

    public void add(Function<String, Inference> constructor) {
        items.add(constructor);
    }

    public Inference valueOf(String str) {
        for (Function<String, Inference> item : items) {
            Inference inf = item.apply(str);
            if (inf != null) {
                return inf;
            }
        }
        return null;
    }
}
