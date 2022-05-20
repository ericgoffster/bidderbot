package bbidder;

import java.util.Objects;

public final class MatchedBid {
    public final Bid bid;
    public final BidPattern pattern;
    public MatchedBid(Bid bid, BidPattern pattern) {
        super();
        this.bid = bid;
        this.pattern = pattern;
    }
    @Override
    public String toString() {
        return "MatchedBid [bid=" + bid + ", pattern=" + pattern + "]";
    }
    @Override
    public int hashCode() {
        return Objects.hash(bid, pattern);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MatchedBid other = (MatchedBid) obj;
        return bid == other.bid && Objects.equals(pattern, other.pattern);
    }
}
