package bbidder;

import java.util.List;

/**
 * A Bridge inference.
 */
public interface Inference {
    public List<MappedInference> bind(InferenceContext context);
    public List<MappedInf> resolveSuits(BiddingContext context);
}
