package bbidder;

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
    public final BidInference bidInference;

    public BoundBidInference(String where, BidInference inferences) {
        super();
        this.where = where;
        this.bidInference = inferences;
    }

    public IBoundInference bind(Players players) {
        return bidInference.inferences.bind(players);
    }
    
    @Override
    public String toString() {
        return bidInference.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(bidInference);
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
        return Objects.equals(bidInference, other.bidInference);
    }
}
