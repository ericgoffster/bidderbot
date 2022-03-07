package bbidder;

import java.util.Objects;

public class BoundBidInference {
    public final BidContext ctx;
    public final InferenceList inferences;

    public BoundBidInference(BidContext ctx, InferenceList inferences) {
        super();
        this.ctx = ctx;
        this.inferences = inferences;
    }

    @Override
    public String toString() {
        return ctx + " => " + inferences;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ctx, inferences);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoundBidInference other = (BoundBidInference) obj;
        return Objects.equals(ctx, other.ctx) && Objects.equals(inferences, other.inferences);
    }
}
