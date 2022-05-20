package bbidder;

import java.util.List;

public final class ResolvedBidInference {
    public final BidInference unresolved;
    public final List<BidInference> inferences;

    public ResolvedBidInference(BidInference unresolved, List<BidInference> inferences) {
        super();
        this.unresolved = unresolved;
        this.inferences = inferences;
    }
}
