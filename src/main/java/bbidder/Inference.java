package bbidder;

import java.util.List;

/**
 * A Bridge inference.
 */
public interface Inference {
    public List<IBoundInference> bind(InferenceContext context);
}
