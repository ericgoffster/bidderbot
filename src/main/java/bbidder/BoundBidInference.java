package bbidder;

import java.util.List;
import java.util.Objects;

/**
 * Represents inferences bound to an actual bidding sequence.
 * These are stored into the compiler bidding system.
 * This structure is immutable.
 * 
 * @author goffster
 *
 */
public class BoundBidInference {
    public final String where;
    public final BiddingContext ctx;
    public final InferenceList inferences;

    public BoundBidInference(String where, BiddingContext ctx, InferenceList inferences) {
        super();
        this.where = where;
        this.ctx = ctx;
        this.inferences = inferences;
    }

    public List<MappedInference> bind(Players players) {
        return inferences.bind(new InferenceContext(players, ctx));
    }
    

    public List<MappedInferenceList> resolveSuits() {
        return inferences.resolveSuits(ctx);
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
