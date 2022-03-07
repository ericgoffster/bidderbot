package bbidder;

/**
 * A Bridge inference.
 */
public interface Inference {
    public IBoundInference bind(InferenceContext context);
}
