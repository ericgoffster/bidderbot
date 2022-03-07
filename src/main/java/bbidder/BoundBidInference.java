package bbidder;

import java.util.Objects;

public class BoundBidInference {
    public final BidList bids;
    public final IBoundInference inference;

    public BoundBidInference(BidList bids, IBoundInference inference) {
        super();
        this.bids = bids;
        this.inference = inference;
    }

    @Override
    public String toString() {
        return bids + " => " + inference;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids, inference);
    }
}
