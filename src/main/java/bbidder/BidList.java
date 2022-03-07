package bbidder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BidList {
    public final List<Bid> bids;

    public BidList(List<Bid> bids) {
        super();
        this.bids = bids;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for(Bid bid: bids) {
            sb.append(delim).append(bid);
            delim = " ";
        }
        return sb.toString();
    }
    
    public BidList addBid(Bid bid) {
        List<Bid> newBids = new ArrayList<>(bids);
        newBids.add(bid);
        return new BidList(newBids);
    }
    
    public static BidList valueOf(String str) {
        String[] parts = str.split("\\s+");
        List<Bid> bids = new ArrayList<>();
        for(String part: parts) {
            bids.add(Bid.fromStr(part.trim()));
        }
        return new BidList(bids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bids);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidList other = (BidList) obj;
        return Objects.equals(bids, other.bids);
    }
}
