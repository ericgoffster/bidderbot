package bbidder;

import java.util.Objects;

public final class TaggedBid {
    public final Bid bid;
    public final TagSet tags;

    public TaggedBid(Bid bid, TagSet tags) {
        super();
        this.bid = bid;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return bid + "" + tags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bid, tags);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaggedBid other = (TaggedBid) obj;
        return bid == other.bid && Objects.equals(tags, other.tags);
    }
}
