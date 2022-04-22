package bbidder;

import java.util.Objects;

/**
 * Represents a possible bid from the bidding system.
 * 
 * @author goffster
 *
 */
public class PossibleBid {
    final BidInference inf;
    final Bid bid;

    public PossibleBid(BidInference inf, Bid bid) {
        super();
        this.inf = inf;
        this.bid = bid;
    }

    @Override
    public String toString() {
        return bid + " : " + inf;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bid, inf);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PossibleBid other = (PossibleBid) obj;
        return bid == other.bid && Objects.equals(inf, other.inf);
    }
}