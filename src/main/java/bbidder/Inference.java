package bbidder;

import java.util.List;
import java.util.Map;

/**
 * A Bridge inference.
 */
public interface Inference {
    public IBoundInference bind(Players players);

    public List<InferenceContext> resolveSymbols(Map<String, Integer> suits);
}
