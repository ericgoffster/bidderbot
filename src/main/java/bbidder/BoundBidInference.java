package bbidder;

import java.util.Objects;

/**
 * Represents inferences bound to an actual bidding sequence.
 * These are stored into the compiler bidding system.
 * 
 * @author goffster
 *
 */
public class BoundBidInference {
    public final BiddingContext ctx;
    public final InferenceList inferences;

    public BoundBidInference(BiddingContext ctx, InferenceList inferences) {
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

    public IBoundInference bind(LikelyHands likelyHands) {
        return inferences.bind(new InferenceContext(ctx.bids.exceptLast().getLastBidSuit(), likelyHands, ctx));
    }
}
