package bbidder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a bid from the bidding system
 * with all bids that could have been made.
 * 
 * @author goffster
 *
 */
public final class BidSource {
    private final List<PossibleBid> possibleBids;
    public final PossibleBid matchedBid;

    public List<PossibleBid> getPossible() {
        return Collections.unmodifiableList(possibleBids);
    }

    BidSource(PossibleBid possibility, List<PossibleBid> possible) {
        super();
        this.possibleBids = possible;
        this.matchedBid = possibility;
    }

    @Override
    public String toString() {
        return matchedBid.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchedBid, possibleBids);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidSource other = (BidSource) obj;
        return Objects.equals(matchedBid, other.matchedBid) && Objects.equals(possibleBids, other.possibleBids);
    }
}