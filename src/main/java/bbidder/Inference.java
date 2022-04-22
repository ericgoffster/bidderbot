package bbidder;

import java.util.List;

/**
 * A Bridge inference.
 */
public interface Inference {
    public IBoundInference bind(Players players);

    public List<BiddingContext> resolveSymbols(BiddingContext context);
}
